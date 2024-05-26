package com.valorant.domain.jdbc.models;

import java.io.Serializable;

/**
 * Represents a map in the Valorant game.
 * Implements the {@link com.valorant.models.Map} interface and Serializable for object serialization.
 */
public class Map implements com.valorant.models.Map, Serializable {

    private int id;         // Unique identifier for the map.
    private String name;    // Name of the map.
    private String type;    // Type or category of the map.

    /**
     * Getter method for retrieving the unique identifier of the map.
     *
     * @return the unique identifier of the map.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Setter method for setting the unique identifier of the map.
     *
     * @param id the unique identifier of the map.
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter method for retrieving the name of the map.
     *
     * @return the name of the map.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Setter method for setting the name of the map.
     *
     * @param name the name of the map.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for retrieving the type of the map.
     *
     * @return the type of the map.
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Setter method for setting the type of the map.
     *
     * @param type the type of the map.
     */
    @Override
    public void setType(String type) {
        this.type = type;
    }
}
