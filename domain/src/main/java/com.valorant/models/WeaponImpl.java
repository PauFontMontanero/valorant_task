package com.valorant.models;

/**
 * Concrete implementation of the {@link Weapon} interface representing a weapon in the Valorant game.
 */
public class WeaponImpl implements Weapon {

    private int id;
    private String name;
    private String type;

    /**
     * Constructor for creating a new weapon instance.
     *
     * @param id   the unique identifier of the weapon.
     * @param name the name of the weapon.
     * @param type the type of the weapon (e.g., pistol, rifle, sniper).
     */
    public WeaponImpl(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    /**
     * Get the unique identifier of the weapon.
     *
     * @return the unique identifier of the weapon.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Set the unique identifier of the weapon.
     *
     * @param id the unique identifier of the weapon.
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the name of the weapon.
     *
     * @return the name of the weapon.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the name of the weapon.
     *
     * @param name the name of the weapon.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the type of the weapon.
     *
     * @return the type of the weapon.
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Set the type of the weapon.
     *
     * @param type the type of the weapon.
     */
    @Override
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Override the toString method to provide a formatted string representation of the WeaponImpl object.
     *
     * @return a string representation of the WeaponImpl object.
     */
    @Override
    public String toString() {
        return "WeaponImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
