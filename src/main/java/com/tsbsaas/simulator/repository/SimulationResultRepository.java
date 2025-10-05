package com.tsbsaas.simulator.repository;

import com.tsbsaas.simulator.model.SimulationResult;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class SimulationResultRepository {

    private final ConcurrentMap<String, SimulationResult> results = new ConcurrentHashMap<>();

    public void save(String id, SimulationResult result) {
        results.put(id, result);
    }

    public SimulationResult findById(String id) {
        return results.get(id);
    }
}
