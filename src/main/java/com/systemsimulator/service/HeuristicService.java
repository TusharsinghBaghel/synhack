package com.systemsimulator.service;

import com.systemsimulator.model.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HeuristicService {

    /**
     * Get default heuristic scores for each component type
     * Scores are on a scale of 0-10 (higher is better)
     */
    public HeuristicProfile getDefaultHeuristicsForType(ComponentType type) {
        Map<Parameter, Double> scores = new HashMap<>();

        switch (type) {
            case DATABASE:
                scores.put(Parameter.LATENCY, 6.0);           // Moderate latency (disk I/O)
                scores.put(Parameter.COST, 7.0);              // Higher cost (persistent storage)
                scores.put(Parameter.SCALABILITY, 7.0);       // Good horizontal scaling
                scores.put(Parameter.CONSISTENCY, 9.0);       // Strong ACID guarantees
                scores.put(Parameter.AVAILABILITY, 8.0);      // High availability with replicas
                scores.put(Parameter.DURABILITY, 9.5);        // Excellent durability
                scores.put(Parameter.MAINTAINABILITY, 6.0);   // Requires schema management
                scores.put(Parameter.ENERGY_EFFICIENCY, 6.0); // Moderate energy use
                scores.put(Parameter.THROUGHPUT, 7.0);        // Good throughput
                scores.put(Parameter.SECURITY, 8.0);          // Strong security features
                break;

            case CACHE:
                scores.put(Parameter.LATENCY, 9.5);           // Extremely low latency (RAM)
                scores.put(Parameter.COST, 5.0);              // Lower cost (ephemeral)
                scores.put(Parameter.SCALABILITY, 8.0);       // Excellent horizontal scaling
                scores.put(Parameter.CONSISTENCY, 6.0);       // Eventual consistency
                scores.put(Parameter.AVAILABILITY, 7.0);      // Good availability
                scores.put(Parameter.DURABILITY, 3.0);        // Low durability (volatile)
                scores.put(Parameter.MAINTAINABILITY, 8.0);   // Easy to manage
                scores.put(Parameter.ENERGY_EFFICIENCY, 7.0); // Good energy efficiency
                scores.put(Parameter.THROUGHPUT, 9.5);        // Excellent throughput
                scores.put(Parameter.SECURITY, 6.0);          // Basic security
                break;

            case API_SERVICE:
                scores.put(Parameter.LATENCY, 7.0);           // Low latency
                scores.put(Parameter.COST, 6.0);              // Moderate cost
                scores.put(Parameter.SCALABILITY, 8.5);       // Excellent horizontal scaling
                scores.put(Parameter.CONSISTENCY, 7.0);       // Application-level consistency
                scores.put(Parameter.AVAILABILITY, 8.0);      // High availability
                scores.put(Parameter.DURABILITY, 5.0);        // Stateless (low durability need)
                scores.put(Parameter.MAINTAINABILITY, 8.0);   // Good maintainability
                scores.put(Parameter.ENERGY_EFFICIENCY, 7.0); // Good efficiency
                scores.put(Parameter.THROUGHPUT, 8.0);        // High throughput
                scores.put(Parameter.SECURITY, 7.5);          // Good security features
                break;

            case QUEUE:
                scores.put(Parameter.LATENCY, 7.5);           // Low latency
                scores.put(Parameter.COST, 6.0);              // Moderate cost
                scores.put(Parameter.SCALABILITY, 9.0);       // Excellent scalability
                scores.put(Parameter.CONSISTENCY, 8.0);       // Strong ordering guarantees
                scores.put(Parameter.AVAILABILITY, 8.5);      // High availability
                scores.put(Parameter.DURABILITY, 8.0);        // Good durability
                scores.put(Parameter.MAINTAINABILITY, 7.5);   // Good maintainability
                scores.put(Parameter.ENERGY_EFFICIENCY, 7.0); // Good efficiency
                scores.put(Parameter.THROUGHPUT, 8.5);        // High throughput
                scores.put(Parameter.SECURITY, 7.0);          // Good security
                break;

            case STORAGE:
                scores.put(Parameter.LATENCY, 5.0);           // Higher latency (disk/network)
                scores.put(Parameter.COST, 8.0);              // Cost-effective for large data
                scores.put(Parameter.SCALABILITY, 9.5);       // Unlimited scaling
                scores.put(Parameter.CONSISTENCY, 7.0);       // Eventual consistency
                scores.put(Parameter.AVAILABILITY, 8.5);      // High availability
                scores.put(Parameter.DURABILITY, 10.0);       // Maximum durability
                scores.put(Parameter.MAINTAINABILITY, 7.0);   // Good maintainability
                scores.put(Parameter.ENERGY_EFFICIENCY, 7.5); // Good efficiency
                scores.put(Parameter.THROUGHPUT, 6.0);        // Moderate throughput
                scores.put(Parameter.SECURITY, 8.5);          // Strong security
                break;

            case LOAD_BALANCER:
                scores.put(Parameter.LATENCY, 8.5);           // Very low latency
                scores.put(Parameter.COST, 5.0);              // Low cost
                scores.put(Parameter.SCALABILITY, 9.0);       // Excellent scalability
                scores.put(Parameter.CONSISTENCY, 7.0);       // Session consistency
                scores.put(Parameter.AVAILABILITY, 9.5);      // Critical for availability
                scores.put(Parameter.DURABILITY, 5.0);        // Stateless
                scores.put(Parameter.MAINTAINABILITY, 8.5);   // Easy to configure
                scores.put(Parameter.ENERGY_EFFICIENCY, 8.0); // Efficient
                scores.put(Parameter.THROUGHPUT, 9.0);        // High throughput
                scores.put(Parameter.SECURITY, 7.5);          // SSL termination, DDoS protection
                break;

            case STREAM_PROCESSOR:
                scores.put(Parameter.LATENCY, 8.0);           // Low latency processing
                scores.put(Parameter.COST, 7.0);              // Higher cost (compute intensive)
                scores.put(Parameter.SCALABILITY, 8.5);       // Good horizontal scaling
                scores.put(Parameter.CONSISTENCY, 7.5);       // Eventual consistency
                scores.put(Parameter.AVAILABILITY, 8.0);      // High availability
                scores.put(Parameter.DURABILITY, 6.0);        // Moderate durability
                scores.put(Parameter.MAINTAINABILITY, 6.5);   // Complex to maintain
                scores.put(Parameter.ENERGY_EFFICIENCY, 6.0); // Compute intensive
                scores.put(Parameter.THROUGHPUT, 9.0);        // Very high throughput
                scores.put(Parameter.SECURITY, 7.0);          // Good security
                break;

            case BATCH_PROCESSOR:
                scores.put(Parameter.LATENCY, 3.0);           // High latency (batch jobs)
                scores.put(Parameter.COST, 8.0);              // Cost-effective for bulk
                scores.put(Parameter.SCALABILITY, 8.0);       // Good scalability
                scores.put(Parameter.CONSISTENCY, 8.5);       // Strong consistency
                scores.put(Parameter.AVAILABILITY, 6.0);      // Not real-time critical
                scores.put(Parameter.DURABILITY, 7.0);        // Good durability
                scores.put(Parameter.MAINTAINABILITY, 6.0);   // Complex jobs
                scores.put(Parameter.ENERGY_EFFICIENCY, 7.0); // Efficient for bulk
                scores.put(Parameter.THROUGHPUT, 7.0);        // High throughput for batches
                scores.put(Parameter.SECURITY, 7.5);          // Good security
                break;

            case EXTERNAL_SERVICE:
                scores.put(Parameter.LATENCY, 5.0);           // Variable latency (network)
                scores.put(Parameter.COST, 6.0);              // Usage-based pricing
                scores.put(Parameter.SCALABILITY, 7.0);       // Depends on provider
                scores.put(Parameter.CONSISTENCY, 6.0);       // Variable consistency
                scores.put(Parameter.AVAILABILITY, 7.0);      // Depends on provider SLA
                scores.put(Parameter.DURABILITY, 7.0);        // Provider-dependent
                scores.put(Parameter.MAINTAINABILITY, 9.0);   // No maintenance required
                scores.put(Parameter.ENERGY_EFFICIENCY, 8.0); // Provider-managed
                scores.put(Parameter.THROUGHPUT, 6.0);        // Rate-limited
                scores.put(Parameter.SECURITY, 6.0);          // Trust third-party
                break;

            case CLIENT:
                scores.put(Parameter.LATENCY, 7.0);           // User-perceived latency
                scores.put(Parameter.COST, 3.0);              // Low infrastructure cost
                scores.put(Parameter.SCALABILITY, 9.0);       // Unlimited clients
                scores.put(Parameter.CONSISTENCY, 5.0);       // Client-side consistency
                scores.put(Parameter.AVAILABILITY, 8.0);      // Always available (offline support)
                scores.put(Parameter.DURABILITY, 4.0);        // Local storage only
                scores.put(Parameter.MAINTAINABILITY, 7.0);   // App updates required
                scores.put(Parameter.ENERGY_EFFICIENCY, 6.0); // Device battery
                scores.put(Parameter.THROUGHPUT, 6.0);        // Limited by network
                scores.put(Parameter.SECURITY, 5.0);          // Client-side vulnerabilities
                break;

            default:
                // Default neutral scores
                for (Parameter param : Parameter.values()) {
                    scores.put(param, 5.0);
                }
        }

        return new HeuristicProfile(scores);
    }

    /**
     * Calculate weighted score for a specific component
     */
    public double calculateComponentScore(Component component, Map<Parameter, Double> weights) {
        return component.getHeuristics().getWeightedScore(weights);
    }

    /**
     * Update heuristic score for a specific parameter
     */
    public void updateHeuristicScore(Component component, Parameter parameter, double score) {
        if (score < 0.0 || score > 10.0) {
            throw new IllegalArgumentException("Score must be between 0.0 and 10.0");
        }
        component.getHeuristics().setScore(parameter, score);
    }

    /**
     * Get heuristic profile with custom adjustments based on properties
     */
    public HeuristicProfile getAdjustedHeuristics(ComponentType type, Map<String, Object> properties) {
        HeuristicProfile baseProfile = getDefaultHeuristicsForType(type);

        // Apply adjustments based on properties
        if (properties != null && !properties.isEmpty()) {
            applyPropertyAdjustments(baseProfile, properties);
        }

        return baseProfile;
    }

    /**
     * Apply property-based adjustments to heuristics
     */
    private void applyPropertyAdjustments(HeuristicProfile profile, Map<String, Object> properties) {
        // Adjust based on replicas
        if (properties.containsKey("replicas")) {
            int replicas = getIntValue(properties.get("replicas"));
            if (replicas > 1) {
                adjustScore(profile, Parameter.AVAILABILITY, 1.0);
                adjustScore(profile, Parameter.DURABILITY, 0.5);
                adjustScore(profile, Parameter.COST, -0.5 * replicas);
            }
        }

        // Adjust based on memory/storage size
        if (properties.containsKey("memoryGB") || properties.containsKey("storageGB")) {
            adjustScore(profile, Parameter.COST, -0.5);
            adjustScore(profile, Parameter.THROUGHPUT, 0.5);
        }

        // Adjust based on instances
        if (properties.containsKey("instances")) {
            int instances = getIntValue(properties.get("instances"));
            if (instances > 1) {
                adjustScore(profile, Parameter.SCALABILITY, 1.0);
                adjustScore(profile, Parameter.AVAILABILITY, 0.5);
                adjustScore(profile, Parameter.COST, -0.3 * instances);
            }
        }
    }

    /**
     * Adjust a parameter score with bounds checking
     */
    private void adjustScore(HeuristicProfile profile, Parameter param, double adjustment) {
        double currentScore = profile.getScore(param);
        double newScore = Math.max(0.0, Math.min(10.0, currentScore + adjustment));
        profile.setScore(param, newScore);
    }

    /**
     * Get integer value from object
     */
    private int getIntValue(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return 1;
            }
        }
        return 1;
    }
}

