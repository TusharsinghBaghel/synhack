package com.tsbsaas.simulator.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tsbsaas.simulator.model.Bucket;
import com.tsbsaas.simulator.model.SimulationResult;
import com.tsbsaas.simulator.model.Run;
import com.tsbsaas.simulator.service.RunOrchestrator;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class SimulationWebSocketHandler extends TextWebSocketHandler {

    private final RunOrchestrator runOrchestrator;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ConcurrentMap<String, Boolean> activeConnections = new ConcurrentHashMap<>();

    public SimulationWebSocketHandler(RunOrchestrator runOrchestrator) {
        this.runOrchestrator = runOrchestrator;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String runId = extractId(session.getUri());
        Run run = runOrchestrator.getRun(runId);
        if (run == null) {
            sendError(session, "RUN_NOT_FOUND", "Run " + runId + " not found");
            session.close();
            return;
        }
        activeConnections.put(session.getId(), true);
        runOrchestrator.registerListener(runId, new RunOrchestrator.BucketListener() {
            @Override
            public void onBucket(Bucket b) {
                if (!session.isOpen()) return;
                ObjectNode node = mapper.createObjectNode();
                node.put("type", "BUCKET");
                node.put("runId", runId);
                node.put("index", b.getIndex());
                node.put("timeMs", b.getTimeStartMs());
                node.put("p50", b.getP50());
                node.put("p95", b.getP95());
                node.put("p99", b.getP99());
                node.put("throughput", b.getThroughput());
                node.put("errors", b.getErrors());
                node.put("costAccrued", b.getCostAccrued());
                node.put("last", b.isLast());
                trySend(session, node);
            }

            @Override
            public void onComplete(SimulationResult summary) {
                if (!session.isOpen()) return;
                ObjectNode node = mapper.createObjectNode();
                node.put("type", "COMPLETE");
                node.put("runId", runId);
                node.set("summary", mapper.valueToTree(summary));
                trySend(session, node);
                try { session.close(); } catch (IOException ignored) {}
            }
        });

        // Send already accumulated buckets (if any) for late subscribers
        runOrchestrator.getBuckets(runId).forEach(b -> {
            ObjectNode node = mapper.createObjectNode();
            node.put("type", "BUCKET");
            node.put("runId", runId);
            node.put("index", b.getIndex());
            node.put("timeMs", b.getTimeStartMs());
            node.put("p50", b.getP50());
            node.put("p95", b.getP95());
            node.put("p99", b.getP99());
            node.put("throughput", b.getThroughput());
            node.put("errors", b.getErrors());
            node.put("costAccrued", b.getCostAccrued());
            node.put("last", b.isLast());
            trySend(session, node);
        });
        // If run is already complete also push summary
        SimulationResult summary = runOrchestrator.getSummary(runId);
        if (summary != null) {
            ObjectNode node = mapper.createObjectNode();
            node.put("type", "COMPLETE");
            node.put("runId", runId);
            node.set("summary", mapper.valueToTree(summary));
            trySend(session, node);
            session.close();
        }
    }

    private void sendError(WebSocketSession session, String code, String message) throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.put("type", "ERROR");
        node.put("code", code);
        node.put("message", message);
        session.sendMessage(new TextMessage(node.toString()));
    }

    private void trySend(WebSocketSession session, ObjectNode node) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(node.toString()));
            }
        } catch (IOException ignored) {}
    }

    private String extractId(URI uri) {
        if (uri == null) return "";
        String path = uri.getPath();
        if (path == null) return "";
        String[] parts = path.split("/");
        return parts.length > 0 ? parts[parts.length - 1] : "";
    }
}
