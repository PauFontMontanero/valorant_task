package com.valorant.domain.jdbc.models;

import com.valorant.models.*;

/**
 * Factory class for creating JDBC implementation instances of game models.
 */
public class JdbcModelFactory implements ModelFactory {

    /**
     * Create an instance of Agent using the JDBC implementation.
     *
     * @return a new instance of Agent.
     */
    @Override
    public Agent createAgent() {
        return new com.valorant.domain.jdbc.models.Agent();
    }

    /**
     * Create an instance of Map using the JDBC implementation.
     *
     * @return a new instance of Map.
     */
    @Override
    public Map createMap() {
        return new com.valorant.domain.jdbc.models.Map();
    }

    /**
     * Create an instance of Match using the JDBC implementation.
     *
     * @return a new instance of Match.
     */
    @Override
    public Match createMatch() {
        return new com.valorant.domain.jdbc.models.Match();
    }

    /**
     * Create an instance of Player using the JDBC implementation.
     *
     * @return a new instance of Player.
     */
    @Override
    public Player createPlayer() {
        return new com.valorant.domain.jdbc.models.Player();
    }

    /**
     * Create an instance of Weapon using the JDBC implementation.
     *
     * @return a new instance of Weapon.
     */
    @Override
    public Weapon createWeapon() {
        return new com.valorant.domain.jdbc.models.Weapon();
    }
}
