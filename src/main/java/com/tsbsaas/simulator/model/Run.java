package com.tsbsaas.simulator.model;

import lombok.Data;
import java.time.Instant;

@Data
public class Run {
    private String id; // runId
    private String scenarioId;
    private Long seed;
    private RunStatus status;
    private Instant startedAt;
    private Instant finishedAt;
    private SimulationResult summary; // filled at completion
}

