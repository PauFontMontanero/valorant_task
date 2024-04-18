package com.valorant.models;

// This interface represents an Agent in the Valorant game.
public interface Agent {
    // Get the unique identifier of the agent.
    int getId();

    // Set the unique identifier of the agent.
    void setId(int id);

    // Get the name of the agent.
    String getName();

    // Set the name of the agent.
    void setName(String name);

    // Get the description of the agent.
    String getDescription();

    // Set the description of the agent.
    void setDescription(String description);

    // Get the role of the agent (e.g., Duelist, Controller, Initiator, or Sentinel).
    String getRole();

    // Set the role of the agent.
    void setRole(String role);
}
