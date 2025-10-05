package com.tsbsaas.simulator.model;

import lombok.Data;
import java.util.Map;

@Data
public class Component {
    private String id;
    private String type; // RDBMS | ColumnDB | Cache | AppServer | Queue
    private Params params; // hardware/resource params
    private Behavior behavior; // concurrency & op profiles
    private CostSKU costSKU; // pricing reference

    @Data
    public static class Params {
        private Integer vCPU;
        private Integer memMB;
        private Integer storageGB;
        private Integer iops;
        private String storageType; // ssd/hdd
    }

    @Data
    public static class Behavior {
        // map operation name -> distribution expression (e.g. Normal(2,0.5))
        private Map<String,String> opProfiles;
        private Integer concurrency; // max in-flight operations
        private Integer queueCapacity; // waiting queue length
        private Double hitRate; // for cache
    }

    @Data
    public static class CostSKU {
        private String provider; // aws / gcp / azure
        private String instance; // e.g. r5.large
        private Double pricePerHour; // USD per hour
    }
}
