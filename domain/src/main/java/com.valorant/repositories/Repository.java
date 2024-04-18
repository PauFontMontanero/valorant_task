package com.valorant.repositories;

import java.util.Set;

// This interface defines basic CRUD operations for entities in a repository.
// K represents the type of the key (usually an identifier) and V represents the type of the value (entity).
public interface Repository<K, V> {

    // Save a new or existing entity to the repository.
    void save(V model);

    // Delete the specified entity from the repository.
    void delete(V model);

    // Retrieve an entity by its unique identifier.
    V get(K id);

    // Retrieve all entities stored in the repository.
    Set<V> getAll();
}
