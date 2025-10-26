package com.systemsimulator.utils;

import com.systemsimulator.model.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HeuristicAggregator {
    //TODO: heuristics must be aggregated parameter wise not overall.. also the aggregation logic must be different for each parameter.. for example latency can be added to aggregate
    /**
     * Aggregates heuristics from all components and links with weighted scoring
     */
    public double aggregate(List<com.systemsimulator.model.Component> components,
                          List<Link> links,
                          ParameterWeights weights) {
        if (components == null || components.isEmpty()) {
            return 0.0;
        }

        double totalScore = 0.0;
        int componentCount = 0;

        // Aggregate component heuristics with weights
        for (com.systemsimulator.model.Component component : components) {
            HeuristicProfile profile = component.getHeuristics();
            double componentScore = profile.getWeightedScore(weights.getDefaultWeights());
            totalScore += componentScore;
            componentCount++;
        }

        // Calculate average component score
        double avgScore = componentCount > 0 ? totalScore / componentCount : 0.0;

        // Apply link factor (bonus/penalty based on connections)
        double linkFactor = calculateLinkFactor(components.size(), links != null ? links.size() : 0);

        return avgScore * linkFactor;
    }

    /**
     * Simple aggregation without weights
     */
    public double aggregateSimple(List<com.systemsimulator.model.Component> components) {
        if (components == null || components.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        int count = 0;

        for (com.systemsimulator.model.Component component : components) {
            for (Double score : component.getHeuristics().getScores().values()) {
                total += score;
                count++;
            }
        }

        return count > 0 ? total / count : 0.0;
    }

    /**
     * Calculate link factor based on architecture complexity
     * Well-connected systems get a bonus, poorly connected or overly complex get penalties
     */
    private double calculateLinkFactor(int componentCount, int linkCount) {
        if (componentCount == 0) {
            return 1.0;
        }

        if (linkCount == 0 && componentCount > 1) {
            return 0.5; // Penalty for disconnected architecture
        }

        // Ideal ratio: 1.5 to 3 links per component
        double ratio = (double) linkCount / componentCount;

        if (ratio < 1.0) {
            // Under-connected: slight penalty
            return 0.8 + (ratio * 0.2);
        } else if (ratio <= 3.0) {
            // Optimal range: full score
            return 1.0;
        } else if (ratio <= 5.0) {
            // Slightly over-connected: minor penalty
            return 1.0 - ((ratio - 3.0) * 0.05);
        } else {
            // Overly complex: significant penalty
            return Math.max(0.7, 1.0 - ((ratio - 3.0) * 0.08));
        }
    }

    /**
     * Calculate bottleneck score - identifies components with too many connections
     */
    public double calculateBottleneckScore(com.systemsimulator.model.Component component,
                                          List<Link> allLinks) {
        if (allLinks == null || allLinks.isEmpty()) {
            return 1.0; // No bottleneck if no links
        }

        int incomingLinks = 0;
        int outgoingLinks = 0;

        for (Link link : allLinks) {
            if (link.getTarget() != null && link.getTarget().getId().equals(component.getId())) {
                incomingLinks++;
            }
            if (link.getSource() != null && link.getSource().getId().equals(component.getId())) {
                outgoingLinks++;
            }
        }

        int totalConnections = incomingLinks + outgoingLinks;

        // Components with more than 10 connections are likely bottlenecks
        if (totalConnections > 10) {
            return 0.5; // High bottleneck risk
        } else if (totalConnections > 5) {
            return 0.7; // Medium bottleneck risk
        } else {
            return 1.0; // Low bottleneck risk
        }
    }

    /**
     * Evaluate parameter-specific scores across architecture
     */
    public Map<Parameter, Double> aggregateByParameter(List<com.systemsimulator.model.Component> components) {
        Map<Parameter, Double> aggregated = new HashMap<>();

        if (components == null || components.isEmpty()) {
            return aggregated;
        }

        for (Parameter param : Parameter.values()) {
            double sum = 0.0;
            int count = 0;

            for (com.systemsimulator.model.Component component : components) {
                Double score = component.getHeuristics().getScore(param);
                if (score != null) {
                    sum += score;
                    count++;
                }
            }

            aggregated.put(param, count > 0 ? sum / count : 0.0);
        }

        return aggregated;
    }

    /**
     * Calculate connectivity score - how well connected the architecture is
     */
    public double calculateConnectivityScore(int componentCount, int linkCount) {
        if (componentCount == 0) {
            return 0.0;
        }

        if (componentCount == 1) {
            return 1.0; // Single component is always "connected"
        }

        // Minimum links needed for connectivity: n-1 (spanning tree)
        int minLinks = componentCount - 1;

        if (linkCount < minLinks) {
            // Disconnected graph
            return (double) linkCount / minLinks * 0.5;
        }

        // Optimal connectivity: 1.5 to 3 links per component
        double ratio = (double) linkCount / componentCount;

        if (ratio >= 1.5 && ratio <= 3.0) {
            return 1.0;
        } else if (ratio < 1.5) {
            return 0.5 + (ratio / 1.5) * 0.5;
        } else {
            // Over-connected
            return Math.max(0.6, 1.0 - ((ratio - 3.0) * 0.1));
        }
    }

    /**
     * Calculate complexity penalty based on architecture size
     */
    public double calculateComplexityPenalty(int componentCount, int linkCount) {
        // Small architectures (< 5 components) - no penalty
        if (componentCount < 5) {
            return 1.0;
        }

        // Medium architectures (5-10 components) - slight penalty
        if (componentCount <= 10) {
            return 0.95;
        }

        // Large architectures (11-20 components) - moderate penalty
        if (componentCount <= 20) {
            return 0.90;
        }

        // Very large architectures (20+ components) - significant penalty
        return Math.max(0.75, 1.0 - (componentCount - 20) * 0.01);
    }

    /**
     * Get weighted average with custom weights map
     */
    public double getWeightedAverage(Map<Parameter, Double> scores, Map<Parameter, Double> weights) {
        if (scores == null || scores.isEmpty()) {
            return 0.0;
        }

        double weightedSum = 0.0;
        double totalWeight = 0.0;

        for (Map.Entry<Parameter, Double> entry : scores.entrySet()) {
            Parameter param = entry.getKey();
            double score = entry.getValue();
            double weight = weights.getOrDefault(param, 1.0);

            weightedSum += score * weight;
            totalWeight += weight;
        }

        return totalWeight > 0 ? weightedSum / totalWeight : 0.0;
    }
}
