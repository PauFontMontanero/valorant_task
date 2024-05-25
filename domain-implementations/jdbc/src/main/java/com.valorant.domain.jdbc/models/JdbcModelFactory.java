package com.valorant.domain.jdbc.models;

import com.valorant.models.*;

public class JdbcModelFactory implements ModelFactory {

    @Override
    public Agent createAgent() {
        return new com.valorant.domain.jdbc.models.Agent();
    }

    @Override
    public Map createMap() {
        return new com.valorant.domain.jdbc.models.Map();
    }

    @Override
    public Match createMatch() {
        return new com.valorant.domain.jdbc.models.Match();
    }

    @Override
    public Player createPlayer() {
        return new com.valorant.domain.jdbc.models.Player();
    }

    @Override
    public Weapon createWeapon() {
        return new com.valorant.domain.jdbc.models.Weapon();
    }
}
