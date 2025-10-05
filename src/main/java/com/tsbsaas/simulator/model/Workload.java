package com.tsbsaas.simulator.model;

import lombok.Data;
import java.util.List;

@Data
public class Workload {
    private String type; // poisson, uniform, etc.
    private Integer rps; // target requests per second
    private Integer durationSec; // total simulated duration
    private List<OpMix> mix; // operation mixture
    private Integer payloadKB; // request payload size
    private Integer hotKeyPct; // % of requests hitting hot subset

    @Data
    public static class OpMix {
        private String op; // operation name referencing component opProfiles keys
        private Integer pct; // percentage (0-100)
    }
}
