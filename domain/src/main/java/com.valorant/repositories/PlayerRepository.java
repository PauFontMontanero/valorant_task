package com.valorant.repositories;

import com.valorant.models.Player;

import java.util.Set;

// This interface defines operations to be performed on Player entities in the repository.
public interface PlayerRepository extends Repository<Integer, Player> {

    // Save a new or existing player to the repository.
    @Override
    void save(Player model);

    // Delete the specified player from the repository.
    @Override
    void delete(Player model);

    // Retrieve a player by its unique identifier.
    @Override
    Player get(Integer id);

    // Retrieve all players stored in the repository.
    @Override
    Set<Player> getAll();

    // Retrieve a player by their username.
    Player getByUsername(String username);

    // Retrieve players belonging to the specified region.
    Set<Player> getByRegion(String region);

    // Retrieve players with the specified display name.
    Set<Player> getByDisplayName(String displayName);
}
