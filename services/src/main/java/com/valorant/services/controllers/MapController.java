package com.valorant.services.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valorant.models.Map;
import com.valorant.repositories.MapRepository;
import com.valorant.services.exception.ResourceNotFoundException;

public class MapController implements Controller<Integer, Map> {
    private final MapRepository repository;
    private final ObjectMapper mapper;

    public MapController(MapRepository repository) {
        this.repository = repository;
        this.mapper = new ObjectMapper();
    }

    @Override
    public String get(Integer id) {
        return findResource(id, Map.class);
    }

    @Override
    public String get() {
        try {
            return mapper.writeValueAsString(repository.getAll());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void post(Map map) {
        repository.save(map);
    }

    @Override
    public void put(Integer key, Map value) {
        validateResourceExists(key);
        repository.save(value);
    }

    @Override
    public void delete(Integer key) {
        validateResourceExists(key);
        repository.delete(repository.get(key));
    }

    private String findResource(Integer id, Class<?> clazz) {
        var resource = repository.get(id);
        if (resource == null) throw new ResourceNotFoundException(clazz.getSimpleName() + " not found");
        try {
            return mapper.writeValueAsString(resource);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateResourceExists(Integer id) {
        if (repository.get(id) == null) throw new ResourceNotFoundException("Map with id " + id + " not found");
    }
}
