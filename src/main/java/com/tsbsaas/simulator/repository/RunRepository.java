package com.tsbsaas.simulator.repository;

import com.tsbsaas.simulator.model.Run;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class RunRepository {
    private final ConcurrentMap<String, Run> runs = new ConcurrentHashMap<>();

    public void save(Run run) { runs.put(run.getId(), run); }
    public Run find(String id) { return runs.get(id); }
}

