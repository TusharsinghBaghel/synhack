package com.systemsimulator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Convert;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name = "components")
public class ComponentDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String name;

    @Enumerated(EnumType.STRING)
    private ComponentType type;

    @Embedded
    private HeuristicProfile heuristics = new HeuristicProfile();

    @Convert(converter = PropertiesConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> properties = new HashMap<>();

    
    public ComponentDb() {}


    public ComponentDb(String name, ComponentType type) {
        this.name = name;
        this.type = type;
    }
    

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ComponentType getType() { return type; }
    public void setType(ComponentType type) { this.type = type; }

    public HeuristicProfile getHeuristics() { return heuristics; }
    public void setHeuristics(HeuristicProfile heuristics) { this.heuristics = heuristics; }

    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}
