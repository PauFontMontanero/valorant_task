package com.valorant.file.repositories;

import com.valorant.models.Weapon;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// Repository implementation for managing weapon data stored in a file.
public class WeaponRepository implements com.valorant.repositories.WeaponRepository {
    private final Map<Integer, Weapon> weapons = new HashMap<>();
    private final String dataPath;  // File path where the data is stored.

    // Constructor to initialize the repository with the data file path.
    public WeaponRepository(String dataPath) {
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
                    // Check if the loaded object is a Map of weapons.
                    if (obj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<Integer, Weapon> loadedWeapons = (Map<Integer, Weapon>) obj;
                        weapons.putAll(loadedWeapons);  // Populate the repository with loaded weapons.
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
            outputStream.writeObject(weapons);  // Write the weapons Map to the file.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Save method to add or update a weapon in the repository and then write the changes to the file.
    @Override
    public void save(Weapon weapon) {
        if (weapon.getId() <= 0) {
            // Generate a new ID if the weapon doesn't have one.
            int newId = weapons.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
            weapon.setId(newId);
        }
        weapons.put(weapon.getId(), weapon);  // Add or update the weapon in the repository.
        write();  // Write the changes to the file.
    }

    // Delete method to remove a weapon from the repository and then write the changes to the file.
    @Override
    public void delete(Weapon weapon) {
        weapons.remove(weapon.getId());  // Remove the weapon from the repository.
        write();  // Write the changes to the file.
    }

    // Method to retrieve a weapon by its ID from the repository.
    @Override
    public Weapon get(Integer id) {
        return weapons.get(id);  // Return the weapon corresponding to the provided ID.
    }

    // Method to retrieve all weapons stored in the repository.
    @Override
    public Set<Weapon> getAll() {
        return Set.copyOf(weapons.values());  // Return a copy of all weapons in the repository.
    }

    // Method to retrieve a weapon by its name from the repository.
    @Override
    public Weapon getByName(String name) {
        return weapons.values().stream()
                .filter(weapon -> weapon.getName().equals(name))
                .findFirst()
                .orElse(null);  // Return the weapon with the specified name, if found.
    }
}
