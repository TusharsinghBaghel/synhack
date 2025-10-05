package com.tsbsaas.simulator.model;

import lombok.Data;

@Data
public class Bucket {
    private long index; // sequential bucket number
    private long timeStartMs; // simulated time start of bucket
    private double p50;
    private double p95;
    private double p99;
    private double throughput; // requests/sec in this bucket
    private long errors;
    private double costAccrued; // cumulative cost up to this bucket
    private boolean last; // marks final bucket
}

