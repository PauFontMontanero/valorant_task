package com.valorant.repositories;

import com.valorant.models.Agent;

import java.util.Set;

// This interface defines operations to be performed on Agent entities in the repository.
public interface AgentRepository extends Repository<Integer, Agent> {

    // Save a new or existing agent to the repository.
    @Override
    void save(Agent model);

    // Delete the specified agent from the repository.
    @Override
    void delete(Agent model);

    // Retrieve an agent by its unique identifier.
    @Override
    Agent get(Integer id);

    // Retrieve all agents stored in the repository.
    @Override
    Set<Agent> getAll();

    // Retrieve an agent by its name.
    Agent getByName(String name);
}
