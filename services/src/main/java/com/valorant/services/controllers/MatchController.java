package com.valorant.services.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.valorant.models.Match;
import com.valorant.repositories.MatchRepository;
import com.valorant.services.exception.ResourceNotFoundException;

import java.util.Set;

public class MatchController implements Controller<Integer, Match> {
    private final MatchRepository repository;
    private final ObjectMapper jsonMapper;

    public MatchController(MatchRepository repository) {
        this.repository = repository;
        this.jsonMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Override
    public String get(Integer id) {
        var match = repository.get(id);
        if (match == null) throw new ResourceNotFoundException("Match ID " + id + " not found");
        try {
            return jsonMapper.writeValueAsString(match);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization error", e);
        }
    }

    @Override
    public String get() {
        try {
            return jsonMapper.writeValueAsString(repository.getAll());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing matches to JSON", e);
        }
    }

    @Override
    public void post(Match match) {
        repository.save(match);
    }

    @Override
    public void put(Integer id, Match match) {
        var existingMatch = repository.get(id);
        if (existingMatch == null) throw new ResourceNotFoundException("Match ID " + id + " not found");
        repository.save(match);
    }

    @Override
    public void delete(Integer id) {
        var match = repository.get(id);
        if (match == null) throw new ResourceNotFoundException("Match ID " + id + " not found");
        repository.delete(match);
    }
}
