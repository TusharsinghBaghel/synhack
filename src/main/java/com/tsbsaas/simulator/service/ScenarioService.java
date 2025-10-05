package com.tsbsaas.simulator.service;

import com.tsbsaas.simulator.model.Scenario;
import com.tsbsaas.simulator.repository.ScenarioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ScenarioService {
    private final ScenarioRepository repository;

    public ScenarioService(ScenarioRepository repository) {
        this.repository = repository;
    }

    public String save(Scenario scenario) {
        String id = scenario.getId() != null ? scenario.getId() : UUID.randomUUID().toString();
        scenario.setId(id);
        repository.save(id, scenario);
        return id;
    }

    public Scenario get(String id) {
        return repository.findById(id);
    }
}

