package com.valorant.services.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valorant.models.Weapon;
import com.valorant.repositories.WeaponRepository;
import com.valorant.services.exception.ResourceNotFoundException;

public class WeaponController implements Controller<Integer, Weapon> {
    private final WeaponRepository repository;
    private final ObjectMapper mapper;

    public WeaponController(WeaponRepository repository) {
        this.repository = repository;
        this.mapper = new ObjectMapper();
    }

    @Override
    public String get(Integer id) {
        return findResource(id, Weapon.class);
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
    public void post(Weapon weapon) {
        repository.save(weapon);
    }

    @Override
    public void put(Integer key, Weapon value) {
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
        if (repository.get(id) == null) throw new ResourceNotFoundException("Weapon with id " + id + " not found");
    }
}
