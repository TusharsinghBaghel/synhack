package com.systemsimulator.model;

import java.util.ArrayList;
import java.util.List;

public class Architecture {
    private String id;
    private String name;
    private List<Component> components = new ArrayList<>();
    private List<Link> links = new ArrayList<>();

    public Architecture() {}

    public Architecture(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Component> getComponents() { return components; }
    public void setComponents(List<Component> components) { this.components = components; }

    public List<Link> getLinks() { return links; }
    public void setLinks(List<Link> links) { this.links = links; }

    public void addComponent(Component component) {
        this.components.add(component);
    }

    public void addLink(Link link) {
        this.links.add(link);
    }
}

