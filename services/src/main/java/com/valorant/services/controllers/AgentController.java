package com.valorant.services.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valorant.models.Agent;
import com.valorant.repositories.AgentRepository;
import com.valorant.services.exception.ResourceNotFoundException;

public class AgentController implements Controller<Integer, Agent> {
    private final AgentRepository repository;
    private final ObjectMapper mapper;

    public AgentController(AgentRepository repository) {
        this.repository = repository;
        this.mapper = new ObjectMapper();
    }

    @Override
    public String get(Integer id) {
        return findResource(id, Agent.class);
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
    public void post(Agent agent) {
        repository.save(agent);
    }

    @Override
    public void put(Integer key, Agent value) {
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
        if (repository.get(id) == null) throw new ResourceNotFoundException("Agent with id " + id + " not found");
    }
}
