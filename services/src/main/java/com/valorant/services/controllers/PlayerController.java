package com.valorant.services.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valorant.models.Player;
import com.valorant.repositories.PlayerRepository;
import com.valorant.services.exception.ResourceNotFoundException;

public class PlayerController implements Controller<Integer, Player> {
    private final PlayerRepository repository;
    private final ObjectMapper jsonMapper;

    public PlayerController(PlayerRepository repository) {
        this.repository = repository;
        this.jsonMapper = new ObjectMapper();
    }

    @Override
    public String get(Integer id) {
        var player = repository.get(id);
        if (player == null) throw new ResourceNotFoundException("Player ID " + id + " not found");
        try {
            return jsonMapper.writeValueAsString(player);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing player to JSON", e);
        }
    }

    @Override
    public String get() {
        try {
            return jsonMapper.writeValueAsString(repository.getAll());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing player list to JSON", e);
        }
    }

    @Override
    public void post(Player player) {
        repository.save(player);
    }

    @Override
    public void put(Integer id, Player player) {
        var existingPlayer = repository.get(id);
        if (existingPlayer == null) throw new ResourceNotFoundException("Player ID " + id + " not found");
        repository.save(player);
    }

    @Override
    public void delete(Integer id) {
        var player = repository.get(id);
        if (player == null) throw new ResourceNotFoundException("Player ID " + id + " not found");
        repository.delete(player);
    }
}
