package com.valorant.file.repositories;

import com.valorant.models.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// Repository implementation for managing player data stored in a file.
public class PlayerRepository implements com.valorant.repositories.PlayerRepository {
    private final Map<Integer, Player> players = new HashMap<>();
    private final String dataPath;  // File path where the data is stored.

    // Constructor to initialize the repository with the data file path.
    public PlayerRepository(String dataPath) {
        this.dataPath = dataPath;
        load();  // Load existing data from the file when the repository is created.
    }

    // Load method to read data from the file and populate the repository.
    protected void load() {
        Path path = Path.of(dataPath);
        try {
            // Check if the file exists and is not empty.
            if (Files.exists(path) && Files.size(path) > 0) {
                try (var inputStream = new ObjectInputStream(new FileInputStream(dataPath))) {
                    Object obj = inputStream.readObject();
                    // Check if the loaded object is a Map of players.
                    if (obj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<Integer, Player> loadedPlayers = (Map<Integer, Player>) obj;
                        players.putAll(loadedPlayers);  // Populate the repository with loaded players.
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
            outputStream.writeObject(players);  // Write the players Map to the file.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Save method to add or update a player in the repository and then write the changes to the file.
    @Override
    public void save(Player player) {
        if (player.getId() <= 0) {
            // Generate a new ID if the player doesn't have one.
            int newId = players.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
            player.setId(newId);
        }
        players.put(player.getId(), player);  // Add or update the player in the repository.
        write();  // Write the changes to the file.
    }

    // Delete method to remove a player from the repository and then write the changes to the file.
    @Override
    public void delete(Player player) {
        players.remove(player.getId());  // Remove the player from the repository.
        write();  // Write the changes to the file.
    }

    // Method to retrieve a player by its ID from the repository.
    @Override
    public Player get(Integer id) {
        return players.get(id);  // Return the player corresponding to the provided ID.
    }

    // Method to retrieve all players stored in the repository.
    @Override
    public Set<Player> getAll() {
        return Set.copyOf(players.values());  // Return a copy of all players in the repository.
    }

    // Method to retrieve a player by their username from the repository.
    @Override
    public Player getByUsername(String username) {
        return players.values().stream()
                .filter(player -> player.getUsername().equals(username))
                .findFirst()
                .orElse(null);  // Return the player with the specified username, if found.
    }

    // Method to retrieve players by their region from the repository.
    @Override
    public Set<Player> getByRegion(String region) {
        return players.values().stream()
                .filter(player -> player.getRegion().equals(region))
                .collect(Collectors.toSet());  // Return players from the specified region.
    }

    // Method to retrieve players by their display name from the repository.
    @Override
    public Set<Player> getByDisplayName(String displayName) {
        return players.values().stream()
                .filter(player -> player.getDisplayName().equals(displayName))
                .collect(Collectors.toSet());  // Return players with the specified display name.
    }
}
