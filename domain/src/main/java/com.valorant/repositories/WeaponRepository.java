package com.valorant.repositories;

import com.valorant.models.Weapon;

import java.util.Set;

// This interface defines operations for managing Weapon entities in a repository.
// It extends the generic Repository interface with specific methods for Weapon entities.
public interface WeaponRepository extends Repository<Integer, Weapon> {

    // Retrieve a weapon by its name.
    Weapon getByName(String name);

    // Override the save method to store a weapon entity in the repository.
    @Override
    void save(Weapon model);

    // Override the delete method to remove a weapon entity from the repository.
    @Override
    void delete(Weapon model);

    // Override the get method to retrieve a weapon by its unique identifier.
    @Override
    Weapon get(Integer id);

    // Override the getAll method to retrieve all weapon entities stored in the repository.
    @Override
    Set<Weapon> getAll();
}
