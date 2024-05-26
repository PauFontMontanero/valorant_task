package com.valorant.domain.jdbc.models;

import java.io.Serializable;

/**
 * Represents an agent in the Valorant game.
 * Implements the {@link com.valorant.models.Agent} interface and Serializable for object serialization.
 */
public class Agent implements com.valorant.models.Agent, Serializable {

    private int id;             // Unique identifier for the agent.
    private String name;        // Name of the agent.
    private String description; // Description or backstory of the agent.
    private String role;        // Role or class of the agent in the game.

    /**
     * Getter method for retrieving the unique identifier of the agent.
     *
     * @return the unique identifier of the agent.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Setter method for setting the unique identifier of the agent.
     *
     * @param id the unique identifier of the agent.
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter method for retrieving the name of the agent.
     *
     * @return the name of the agent.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Setter method for setting the name of the agent.
     *
     * @param name the name of the agent.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for retrieving the description of the agent.
     *
     * @return the description of the agent.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Setter method for setting the description of the agent.
     *
     * @param description the description of the agent.
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter method for retrieving the role of the agent.
     *
     * @return the role of the agent.
     */
    @Override
    public String getRole() {
        return role;
    }

    /**
     * Setter method for setting the role of the agent.
     *
     * @param role the role of the agent.
     */
    @Override
    public void setRole(String role) {
        this.role = role;
    }
}
