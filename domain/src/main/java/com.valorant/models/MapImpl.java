package com.valorant.models;

// A concrete implementation of the Map interface
public class MapImpl implements Map {

    private int id;
    private String name;
    private String type;

    /**
     * Get the unique identifier of the map.
     *
     * @return the unique identifier of the map
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Set the unique identifier of the map.
     *
     * @param id the unique identifier of the map
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the name of the map.
     *
     * @return the name of the map
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the name of the map.
     *
     * @param name the name of the map
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the type of the map (e.g., Standard, Custom, Competitive).
     *
     * @return the type of the map
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Set the type of the map.
     *
     * @param type the type of the map
     */
    @Override
    public void setType(String type) {
        this.type = type;
    }
}
