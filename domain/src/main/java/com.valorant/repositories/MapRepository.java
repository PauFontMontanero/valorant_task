package com.valorant.repositories;

import com.valorant.models.Map;

import java.util.Set;

// This interface defines operations to be performed on Map entities in the repository.
public interface MapRepository extends Repository<Integer, Map> {

    // Retrieve a map by its name.
    Map getByName(String name);

    // Save a new or existing map to the repository.
    @Override
    void save(Map model);

    // Delete the specified map from the repository.
    @Override
    void delete(Map model);

    // Retrieve a map by its unique identifier.
    @Override
    Map get(Integer id);

    // Retrieve all maps stored in the repository.
    @Override
    Set<Map> getAll();
}
