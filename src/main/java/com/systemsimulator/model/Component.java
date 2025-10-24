package com.systemsimulator.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
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

}
