package com.systemsimulator.service;

import com.systemsimulator.model.*;
import com.systemsimulator.repository.InMemoryComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ComponentService {

    @Autowired
    private InMemoryComponentRepository componentRepository;

    @Autowired
    private HeuristicService heuristicService;

    public Component createComponent(ComponentType type, String id, String name, Map<String, Object> properties) {
        Component component = instantiateComponent(type, id, name);
        component.setProperties(properties);

        // Initialize default heuristics
        HeuristicProfile heuristics = heuristicService.getDefaultHeuristicsForType(type);
        component.setHeuristics(heuristics);

        return componentRepository.save(component);
    }

    public Component saveComponent(Component component) {
        return componentRepository.save(component);
    }

    public Optional<Component> getComponentById(String id) {
        return componentRepository.findById(id);
    }

    public List<Component> getAllComponents() {
        return componentRepository.findAll();
    }

    public void deleteComponent(String id) {
        componentRepository.deleteById(id);
    }

    public boolean componentExists(String id) {
        return componentRepository.existsById(id);
    }

    private Component instantiateComponent(ComponentType type, String id, String name) {
        switch (type) {
            case DATABASE:
                return new DatabaseComponent(id, name, DatabaseComponent.DatabaseType.SQL);
            case CACHE:
                return new CacheComponent(id, name, CacheComponent.CacheType.IN_MEMORY);
            case API_SERVICE:
                return new APIServiceComponent(id, name, APIServiceComponent.APIType.REST);
            case QUEUE:
                return new QueueComponent(id, name, QueueComponent.QueueType.MESSAGE_QUEUE);
            case STORAGE:
                return new StorageComponent(id, name, StorageComponent.StorageType.OBJECT_STORAGE);
            case LOAD_BALANCER:
                return new LoadBalancerComponent(id, name, LoadBalancerComponent.LoadBalancerType.ROUND_ROBIN);
            default:
                throw new IllegalArgumentException("Unsupported component type: " + type);
        }
    }
}

