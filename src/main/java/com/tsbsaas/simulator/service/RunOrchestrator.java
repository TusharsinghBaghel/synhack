package com.tsbsaas.simulator.service;

import com.tsbsaas.simulator.model.*;
import com.tsbsaas.simulator.repository.RunRepository;
import com.tsbsaas.simulator.repository.ScenarioRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

@Service
public class RunOrchestrator {

    private final ScenarioRepository scenarioRepository;
    private final RunRepository runRepository;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);

    private final Map<String, List<BucketListener>> bucketListeners = new ConcurrentHashMap<>();
    private final Map<String, List<Bucket>> bucketStore = new ConcurrentHashMap<>();
    private final Map<String, SimulationResult> summaries = new ConcurrentHashMap<>();

    public RunOrchestrator(ScenarioRepository scenarioRepository, RunRepository runRepository) {
        this.scenarioRepository = scenarioRepository;
        this.runRepository = runRepository;
    }

    public String startRun(Scenario scenario, Long seedOverride, Integer durationSec) {
        if (scenario.getId() == null) {
            scenario.setId(UUID.randomUUID().toString());
            scenarioRepository.save(scenario.getId(), scenario);
        }
        String runId = UUID.randomUUID().toString();
        Run run = new Run();
        run.setId(runId);
        run.setScenarioId(scenario.getId());
        run.setSeed(seedOverride != null ? seedOverride : Optional.ofNullable(scenario.getSeed()).orElse(System.currentTimeMillis()));
        run.setStatus(RunStatus.PENDING);
        runRepository.save(run);
        int totalDuration = durationSec != null ? durationSec : Optional.ofNullable(scenario.getWorkload()).map(Workload::getDurationSec).orElse(60);
        scheduleRun(runId, scenario, totalDuration);
        return runId;
    }

    private void scheduleRun(String runId, Scenario scenario, int durationSec) {
        Run run = runRepository.find(runId);
        if (run == null) return;
        run.setStatus(RunStatus.RUNNING);
        run.setStartedAt(Instant.now());
        runRepository.save(run);

        long buckets = Math.max(1, durationSec * 10L); // bucket every 100ms simulated
        Random rng = new Random(run.getSeed());
        bucketStore.put(runId, new CopyOnWriteArrayList<>());

        for (int i = 0; i < buckets; i++) {
            int idx = i;
            executor.schedule(() -> emitBucket(runId, idx, buckets, rng), i * 100L, TimeUnit.MILLISECONDS);
        }

        executor.schedule(() -> finalizeRun(runId), buckets * 100L + 100, TimeUnit.MILLISECONDS);
    }

    private void emitBucket(String runId, int idx, long total, Random rng) {
        List<Bucket> list = bucketStore.get(runId);
        if (list == null) return;
        Bucket b = new Bucket();
        b.setIndex(idx);
        b.setTimeStartMs(idx * 100L);
        // Fake metrics
        b.setP50(5 + rng.nextDouble() * 5);
        b.setP95(10 + rng.nextDouble() * 15);
        b.setP99(20 + rng.nextDouble() * 20);
        b.setThroughput(100 + rng.nextDouble() * 50);
        b.setErrors(rng.nextDouble() < 0.05 ? rng.nextInt(3) : 0);
        double prevCost = list.isEmpty() ? 0.0 : list.get(list.size() - 1).getCostAccrued();
        b.setCostAccrued(prevCost + (0.0001 * b.getThroughput()));
        b.setLast(idx == total - 1);
        list.add(b);
        dispatchBucket(runId, b);
    }

    private void finalizeRun(String runId) {
        Run run = runRepository.find(runId);
        if (run == null) return;
        List<Bucket> buckets = bucketStore.getOrDefault(runId, List.of());
        SimulationResult summary = aggregate(buckets, runId);
        summaries.put(runId, summary);
        run.setStatus(RunStatus.COMPLETED);
        run.setFinishedAt(Instant.now());
        run.setSummary(summary);
        runRepository.save(run);
        dispatchComplete(runId, summary);
    }

    private SimulationResult aggregate(List<Bucket> buckets, String runId) {
        SimulationResult r = new SimulationResult();
        r.setId(runId);
        if (buckets.isEmpty()) return r;
        r.setP50Latency(buckets.stream().mapToDouble(Bucket::getP50).average().orElse(0));
        r.setP95Latency(buckets.stream().mapToDouble(Bucket::getP95).average().orElse(0));
        r.setP99Latency(buckets.stream().mapToDouble(Bucket::getP99).average().orElse(0));
        r.setP999Latency(r.getP99Latency() * 1.2);
        long totalReq = Math.round(buckets.stream().mapToDouble(Bucket::getThroughput).sum() * 0.1); // since buckets represent 0.1s
        r.setTotalRequests(totalReq);
        r.setThroughputRps(buckets.stream().mapToDouble(Bucket::getThroughput).average().orElse(0));
        r.setErrorRate( computeErrorRate(buckets, totalReq) );
        r.setTotalCost(buckets.isEmpty() ? 0 : buckets.get(buckets.size()-1).getCostAccrued());
        r.setCostPerMillion(totalReq == 0 ? 0 : r.getTotalCost() / (totalReq / 1_000_000.0));
        return r;
    }

    private double computeErrorRate(List<Bucket> buckets, long totalReq) {
        long errors = buckets.stream().mapToLong(Bucket::getErrors).sum();
        return totalReq == 0 ? 0 : (errors * 100.0 / totalReq);
    }

    public Run getRun(String id) { return runRepository.find(id); }
    public SimulationResult getSummary(String runId) { return summaries.get(runId); }
    public List<Bucket> getBuckets(String runId) { return bucketStore.getOrDefault(runId, List.of()); }

    public void registerListener(String runId, BucketListener listener) {
        bucketListeners.computeIfAbsent(runId, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    private void dispatchBucket(String runId, Bucket b) {
        bucketListeners.getOrDefault(runId, List.of()).forEach(l -> l.onBucket(b));
    }

    private void dispatchComplete(String runId, SimulationResult summary) {
        bucketListeners.getOrDefault(runId, List.of()).forEach(l -> l.onComplete(summary));
    }

    public interface BucketListener {
        void onBucket(Bucket b);
        void onComplete(SimulationResult summary);
    }
}

