package com.systemsimulator.repository;

import com.systemsimulator.model.Component;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentRepository extends MongoRepository<Component, String> {
    // You can add custom query methods later if needed, e.g.:
    // Optional<Component> findByName(String name);
}
