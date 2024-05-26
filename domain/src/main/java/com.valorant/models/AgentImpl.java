package com.valorant.models;

// Concrete implementation of the Agent interface
public class AgentImpl implements Agent {

    private int id;
    private String name;
    private String description;
    private String role;

    /**
     * Get the unique identifier of the agent.
     *
     * @return the unique identifier of the agent.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Set the unique identifier of the agent.
     *
     * @param id the unique identifier of the agent.
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the name of the agent.
     *
     * @return the name of the agent.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the name of the agent.
     *
     * @param name the name of the agent.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the description of the agent.
     *
     * @return the description of the agent.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the agent.
     *
     * @param description the description of the agent.
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the role of the agent.
     *
     * @return the role of the agent.
     */
    @Override
    public String getRole() {
        return role;
    }

    /**
     * Set the role of the agent.
     *
     * @param role the role of the agent.
     */
    @Override
    public void setRole(String role) {
        this.role = role;
    }
}
