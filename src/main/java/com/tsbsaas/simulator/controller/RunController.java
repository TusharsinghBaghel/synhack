package com.tsbsaas.simulator.controller;

import com.tsbsaas.simulator.model.*;
import com.tsbsaas.simulator.service.RunOrchestrator;
import com.tsbsaas.simulator.service.ScenarioService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/runs")
public class RunController {

    private final RunOrchestrator orchestrator;
    private final ScenarioService scenarioService;

    public RunController(RunOrchestrator orchestrator, ScenarioService scenarioService) {
        this.orchestrator = orchestrator;
        this.scenarioService = scenarioService;
    }

    @PostMapping
    public ResponseEntity<RunIdResponse> start(@RequestBody RunRequest req) {
        Scenario scenario;
        if (req.getScenarioId() != null) {
            scenario = scenarioService.get(req.getScenarioId());
            if (scenario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else if (req.getScenario() != null) {
            String id = scenarioService.save(req.getScenario());
            scenario = scenarioService.get(id);
        } else {
            return ResponseEntity.badRequest().build();
        }
        String runId = orchestrator.startRun(scenario, req.getSeed(), req.getDurationSec());
        return ResponseEntity.ok(new RunIdResponse(runId));
    }

    @GetMapping("/{runId}")
    public ResponseEntity<Run> get(@PathVariable String runId) {
        Run r = orchestrator.getRun(runId);
        return r == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(r);
    }

    @GetMapping("/{runId}/summary")
    public ResponseEntity<SimulationResult> summary(@PathVariable String runId) {
        SimulationResult s = orchestrator.getSummary(runId);
        return s == null ? ResponseEntity.status(HttpStatus.ACCEPTED).build() : ResponseEntity.ok(s);
    }

    @GetMapping("/{runId}/buckets")
    public ResponseEntity<List<Bucket>> buckets(@PathVariable String runId) {
        Run r = orchestrator.getRun(runId);
        if (r == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(orchestrator.getBuckets(runId));
    }

    @Data
    public static class RunRequest {
        private String scenarioId;
        private Scenario scenario;
        private Long seed;
        private Integer durationSec; // overrides scenario workload duration
    }

    @Data
    public static class RunIdResponse {
        private final String runId;
    }
}

