package com.valorant.domain.jdbc.models;

import java.io.Serializable;

/**
 * Represents a weapon in the Valorant game.
 * Implements the {@link com.valorant.models.Weapon} interface and Serializable for object serialization.
 */
public class Weapon implements com.valorant.models.Weapon, Serializable {
    private int id;         // Unique identifier for the weapon.
    private String name;    // Name of the weapon.
    private String type;    // Type of the weapon.

    /**
     * Getter method for retrieving the unique identifier of the weapon.
     *
     * @return the unique identifier of the weapon.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Setter method for setting the unique identifier of the weapon.
     *
     * @param id the unique identifier of the weapon.
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter method for retrieving the name of the weapon.
     *
     * @return the name of the weapon.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Setter method for setting the name of the weapon.
     *
     * @param name the name of the weapon.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for retrieving the type of the weapon.
     *
     * @return the type of the weapon.
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Setter method for setting the type of the weapon.
     *
     * @param type the type of the weapon.
     */
    @Override
    public void setType(String type) {
        this.type = type;
    }
}
