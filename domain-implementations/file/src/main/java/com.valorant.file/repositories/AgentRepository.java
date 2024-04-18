package com.valorant.file.repositories;

import com.valorant.models.Agent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// Repository implementation for managing agents' data stored in a file.
public class AgentRepository implements com.valorant.repositories.AgentRepository {
    private final Map<Integer, Agent> agents = new HashMap<>();
    private final String dataPath;  // File path where the data is stored.

    // Constructor to initialize the repository with the data file path.
    public AgentRepository(String dataPath) {
        this.dataPath = dataPath;
        load();  // Load existing data from the file when the repository is created.
    }

    // Load method to read data from the file and populate the repository.
    protected void load() {
        try {
            Path path = Path.of(dataPath);
            // Check if the file exists and is not empty.
            if (Files.exists(path) && Files.readAllBytes(path).length > 0) {
                try (var inputStream = new ObjectInputStream(new FileInputStream(dataPath))) {
                    Object obj = inputStream.readObject();
                    // Check if the loaded object is a Map of agents.
                    if (obj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<Integer, Agent> loadedAgents = (Map<Integer, Agent>) obj;
                        agents.putAll(loadedAgents);  // Populate the repository with loaded agents.
                    } else {
                        throw new RuntimeException("Invalid data format");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to write the repository data back to the file.
    private void write() {
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(dataPath))) {
            outputStream.writeObject(agents);  // Write the agents map to the file.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Save method to add or update an agent in the repository and then write the changes to the file.
    @Override
    public void save(Agent agent) {
        if (agent.getId() <= 0) {
            // Generate a new ID if the agent doesn't have one.
            int newId = agents.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
            agent.setId(newId);
        }
        agents.put(agent.getId(), agent);  // Add or update the agent in the repository.
        write();  // Write the changes to the file.
    }

    // Delete method to remove an agent from the repository and then write the changes to the file.
    @Override
    public void delete(Agent agent) {
        agents.remove(agent.getId());  // Remove the agent from the repository.
        write();  // Write the changes to the file.
    }

    // Method to retrieve an agent by its ID from the repository.
    @Override
    public Agent get(Integer id) {
        return agents.get(id);  // Return the agent corresponding to the provided ID.
    }

    // Method to retrieve all agents stored in the repository.
    @Override
    public Set<Agent> getAll() {
        return Set.copyOf(agents.values());  // Return a copy of all agents in the repository.
    }

    // Method to retrieve an agent by its name from the repository.
    @Override
    public Agent getByName(String name) {
        return agents.values().stream()
                .filter(agent -> agent.getName().equals(name))
                .findFirst()
                .orElse(null);  // Return the first agent with the specified name or null if not found.
    }
}
