package com.tsbsaas.simulator.model;

import lombok.Data;

@Data
public class SimulationResult {
    private String id; // runId
    private double p50Latency;
    private double p95Latency;
    private double p99Latency;
    private double p999Latency;
    private double errorRate; // %
    private long totalRequests;
    private double throughputRps; // average
    private double totalCost; // accumulated cost
    private double costPerMillion; // normalized cost
}
