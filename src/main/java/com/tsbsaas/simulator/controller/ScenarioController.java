package com.tsbsaas.simulator.controller;

import com.tsbsaas.simulator.model.Scenario;
import com.tsbsaas.simulator.service.ScenarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scenarios")
public class ScenarioController {

    private final ScenarioService scenarioService;

    public ScenarioController(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @PostMapping
    public String create(@RequestBody Scenario scenario) {
        return scenarioService.save(scenario);
    }

    @GetMapping("/{id}")
    public Scenario get(@PathVariable String id) {
        return scenarioService.get(id);
    }
}
