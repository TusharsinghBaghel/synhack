package com.systemsimulator.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
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

    public void addComponent(Component component) {
        this.components.add(component);
    }

    public void addLink(Link link) {
        this.links.add(link);
    }
}

