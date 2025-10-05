package com.tsbsaas.simulator.model;

import lombok.Data;

@Data
public class Link {
    private String id; // new unique link id
    private String from;
    private String to;
    // New distribution DSL (e.g., Normal(3,1))
    private String latencyDistribution;
    private Integer bandwidthMbps; // nullable for unspecified
    private Double lossRate; // 0..1
    // Legacy latency (ms) kept for backward compatibility; prefer latencyDistribution
    private Integer latency;
}
