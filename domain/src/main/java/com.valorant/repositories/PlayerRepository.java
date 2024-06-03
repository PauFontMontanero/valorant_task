package com.valorant.repositories;

import com.valorant.models.Player;

import java.util.Set;

public interface PlayerRepository extends Repository<Integer, Player> {

    @Override
    void save(Player model);

    @Override
    void delete(Player model);

    @Override
    Player get(Integer id);

    @Override
    Set<Player> getAll();

    Player getByUsername(String username);

    Set<Player> getByRegion(String region);

    Set<Player> getByDisplayName(String displayName);
}
