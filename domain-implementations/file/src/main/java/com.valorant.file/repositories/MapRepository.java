package com.valorant.file.repositories;

import com.valorant.models.Map;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

// Repository implementation for managing map data stored in a file.
public class MapRepository implements com.valorant.repositories.MapRepository {
    private final HashMap<Integer, Map> maps = new HashMap<>();
    private final String dataPath;  // File path where the data is stored.

    // Constructor to initialize the repository with the data file path.
    public MapRepository(String dataPath) {
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
                    // Check if the loaded object is a HashMap of maps.
                    if (obj instanceof HashMap) {
                        @SuppressWarnings("unchecked")
                        HashMap<Integer, Map> loadedMaps = (HashMap<Integer, Map>) obj;
                        maps.putAll(loadedMaps);  // Populate the repository with loaded maps.
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
            outputStream.writeObject(maps);  // Write the maps HashMap to the file.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Save method to add or update a map in the repository and then write the changes to the file.
    @Override
    public void save(Map map) {
        if (map.getId() <= 0) {
            // Generate a new ID if the map doesn't have one.
            int newId = maps.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
            map.setId(newId);
        }
        maps.put(map.getId(), map);  // Add or update the map in the repository.
        write();  // Write the changes to the file.
    }

    // Delete method to remove a map from the repository and then write the changes to the file.
    @Override
    public void delete(Map map) {
        maps.remove(map.getId());  // Remove the map from the repository.
        write();  // Write the changes to the file.
    }

    // Method to retrieve a map by its ID from the repository.
    @Override
    public Map get(Integer id) {
        return maps.get(id);  // Return the map corresponding to the provided ID.
    }

    // Method to retrieve all maps stored in the repository.
    @Override
    public Set<Map> getAll() {
        return new HashSet<>(maps.values());  // Return a copy of all maps in the repository.
    }

    // Method to retrieve a map by its name from the repository.
    @Override
    public Map getByName(String name) {
        return maps.values().stream()
                .filter(map -> map.getName().equals(name))
                .findFirst()
                .orElse(null);  // Return the first map with the specified name or null if not found.
    }
}
