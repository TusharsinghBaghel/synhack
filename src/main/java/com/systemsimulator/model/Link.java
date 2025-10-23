package com.systemsimulator.model;

import java.util.HashMap;
import java.util.Map;

public class Link {
    private String id;
    private Component source;
    private Component target;
    private LinkType type;
    private Map<String, Object> properties = new HashMap<>();

    public Link() {}

    public Link(String id, Component source, Component target, LinkType type) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Component getSource() { return source; }
    public void setSource(Component source) { this.source = source; }

    public Component getTarget() { return target; }
    public void setTarget(Component target) { this.target = target; }

    public LinkType getType() { return type; }
    public void setType(LinkType type) { this.type = type; }

    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}

