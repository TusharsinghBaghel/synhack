package com.tsbsaas.simulator.model;

import lombok.Data;
import java.util.List;

@Data
public class Scenario {
    private String id; // uuid (assigned server-side if null)
    private String name;
    private Long seed; // optional deterministic seed
    private List<Component> components;
    private List<Link> links;
    private Workload workload;
    private List<Campaign> campaigns; // optional fault campaigns

    @Data
    public static class Campaign {
        private String id;
        private String type; // FAULT, CHAOS, etc.
        private Long startMs;
        private Long durationMs;
        private Double latencyMultiplier;
        private Double dropRate; // 0..1
    }
}
