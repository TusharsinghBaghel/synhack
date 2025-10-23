package com.systemsimulator.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Component {
    private String id;
    private String name;
    private ComponentType type;
    private HeuristicProfile heuristics = new HeuristicProfile();
    private Map<String, Object> properties = new HashMap<>();

    public Component() {}

    public Component(String id, String name, ComponentType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ComponentType getType() { return type; }
    public void setType(ComponentType type) { this.type = type; }

    public HeuristicProfile getHeuristics() { return heuristics; }
    public void setHeuristics(HeuristicProfile heuristics) { this.heuristics = heuristics; }

    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}
