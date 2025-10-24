package com.systemsimulator.controller;

import com.systemsimulator.model.*;
import com.systemsimulator.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/components")
@CrossOrigin(origins = "*")
public class ComponentController {

    @Autowired
    private ComponentService componentService;

    /**
     * Get all components
     */
    @GetMapping
    public ResponseEntity<List<Component>> getAllComponents() {
        return ResponseEntity.ok(componentService.getAllComponents());
    }

    /**
     * Get component by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getComponentById(@PathVariable String id) {
        return componentService.getComponentById(id)
                .map(component -> ResponseEntity.ok((Object) component))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Component not found: " + id)));
    }

    /**
     * Create a new component
     */
    @PostMapping
    public ResponseEntity<?> createComponent(@RequestBody ComponentRequest request) {
        try {
            String id = UUID.randomUUID().toString();
            Component component = componentService.createComponent(
                    request.getType(),
                    id,
                    request.getName(),
                    request.getProperties() != null ? request.getProperties() : Map.of()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(component);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Failed to create component: " + e.getMessage()));
        }
    }

    /**
     * Update an existing component
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComponent(@PathVariable String id, @RequestBody Component component) {
        if (!componentService.componentExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Component not found: " + id));
        }
        component.setId(id);
        Component updated = componentService.saveComponent(component);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a component
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComponent(@PathVariable String id) {
        if (!componentService.componentExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Component not found: " + id));
        }
        componentService.deleteComponent(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get component types
     */
    @GetMapping("/types")
    public ResponseEntity<ComponentType[]> getComponentTypes() {
        return ResponseEntity.ok(ComponentType.values());
    }

    /**
     * Get component count
     */
    @GetMapping("/count")
    public ResponseEntity<CountResponse> getComponentCount() {
        int count = componentService.getAllComponents().size();
        return ResponseEntity.ok(new CountResponse(count));
    }

    /**
     * Get components by type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Component>> getComponentsByType(@PathVariable ComponentType type) {
        List<Component> components = componentService.getAllComponents().stream()
                .filter(c -> c.getType() == type)
                .toList();
        return ResponseEntity.ok(components);
    }

    /**
     * Check if component exists
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<ExistsResponse> checkComponentExists(@PathVariable String id) {
        boolean exists = componentService.componentExists(id);
        return ResponseEntity.ok(new ExistsResponse(id, exists));
    }

    // ==================== DTOs ====================

    public static class ComponentRequest {
        private ComponentType type;
        private String name;
        private Map<String, Object> properties;

        public ComponentType getType() { return type; }
        public void setType(ComponentType type) { this.type = type; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Map<String, Object> getProperties() { return properties; }
        public void setProperties(Map<String, Object> properties) { this.properties = properties; }
    }

    public static class ErrorResponse {
        private String error;
        private long timestamp;

        public ErrorResponse(String error) {
            this.error = error;
            this.timestamp = System.currentTimeMillis();
        }

        public String getError() { return error; }
        public long getTimestamp() { return timestamp; }
    }

    public static class CountResponse {
        private int count;

        public CountResponse(int count) {
            this.count = count;
        }

        public int getCount() { return count; }
    }

    public static class ExistsResponse {
        private String id;
        private boolean exists;

        public ExistsResponse(String id, boolean exists) {
            this.id = id;
            this.exists = exists;
        }

        public String getId() { return id; }
        public boolean isExists() { return exists; }
    }
}
