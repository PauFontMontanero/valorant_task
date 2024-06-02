package com.valorant.domain.jpa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "WEAPON")
public class WeaponEntity implements com.valorant.models.Weapon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WEAPON_ID")
    private int id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "TYPE", nullable = false)
    private String type;

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
}
