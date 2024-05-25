package com.valorant.models;

// Concrete implementation of the Weapon interface.
public class WeaponImpl implements Weapon {
    private int id;
    private String name;
    private String type;

    // Constructor
    public WeaponImpl(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    // Getters and setters
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    // Override toString for better readability
    @Override
    public String toString() {
        return "WeaponImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
