package com.tsbsaas.simulator.repository;

import com.tsbsaas.simulator.model.Scenario;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class ScenarioRepository {

    private final ConcurrentMap<String, Scenario> scenarios = new ConcurrentHashMap<>();

    public void save(String id, Scenario scenario) {
        scenarios.put(id, scenario);
    }

    public Scenario findById(String id) {
        return scenarios.get(id);
    }
}
