package com.valorant.models;

// This interface represents a Map in the Valorant game.
public interface Map {
    // Get the unique identifier of the map.
    int getId();

    // Set the unique identifier of the map.
    void setId(int id);

    // Get the name of the map.
    String getName();

    // Set the name of the map.
    void setName(String name);

    // Get the type of the map (e.g., Standard, Custom, Competitive).
    String getType();

    // Set the type of the map.
    void setType(String type);
}