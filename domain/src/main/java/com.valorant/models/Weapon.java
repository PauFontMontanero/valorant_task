package com.valorant.models;

// This interface represents a Weapon in the Valorant game.
public interface Weapon {
    // Get the unique identifier of the weapon.
    int getId();

    // Set the unique identifier of the weapon.
    void setId(int id);

    // Get the name of the weapon.
    String getName();

    // Set the name of the weapon.
    void setName(String name);

    // Get the type of the weapon (e.g., pistol, rifle, sniper).
    String getType();

    // Set the type of the weapon.
    void setType(String type);
}
