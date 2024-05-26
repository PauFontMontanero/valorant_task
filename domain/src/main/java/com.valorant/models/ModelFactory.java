package com.valorant.models;

/**
 * The ModelFactory interface defines methods for creating instances of various game entities.
 * Implementations of this interface should provide concrete implementations for creating
 * objects representing Agents, Maps, Matches, Players, and Weapons.
 */
public interface ModelFactory {

    /**
     * Creates a new instance of an Agent.
     *
     * @return a new Agent instance.
     */
    Agent createAgent();

    /**
     * Creates a new instance of a Map.
     *
     * @return a new Map instance.
     */
    Map createMap();

    /**
     * Creates a new instance of a Match.
     *
     * @return a new Match instance.
     */
    Match createMatch();

    /**
     * Creates a new instance of a Player.
     *
     * @return a new Player instance.
     */
    Player createPlayer();

    /**
     * Creates a new instance of a Weapon.
     *
     * @return a new Weapon instance.
     */
    Weapon createWeapon();
}
