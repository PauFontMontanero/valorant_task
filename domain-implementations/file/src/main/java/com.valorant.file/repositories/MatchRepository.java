package com.valorant.file.repositories;

import com.valorant.models.Match;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// Repository implementation for managing match data stored in a file.
public class MatchRepository implements com.valorant.repositories.MatchRepository {
    private final Map<Integer, Match> matches = new HashMap<>();
    private final String dataPath;  // File path where the data is stored.

    // Constructor to initialize the repository with the data file path.
    public MatchRepository(String dataPath) {
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
                    // Check if the loaded object is a Map of matches.
                    if (obj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<Integer, Match> loadedMatches = (Map<Integer, Match>) obj;
                        matches.putAll(loadedMatches);  // Populate the repository with loaded matches.
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
            outputStream.writeObject(matches);  // Write the matches Map to the file.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Save method to add or update a match in the repository and then write the changes to the file.
    @Override
    public void save(Match match) {
        if (match.getId() <= 0) {
            // Generate a new ID if the match doesn't have one.
            int newId = matches.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
            match.setId(newId);
        }
        matches.put(match.getId(), match);  // Add or update the match in the repository.
        write();  // Write the changes to the file.
    }

    // Delete method to remove a match from the repository and then write the changes to the file.
    @Override
    public void delete(Match match) {
        matches.remove(match.getId());  // Remove the match from the repository.
        write();  // Write the changes to the file.
    }

    // Method to retrieve a match by its ID from the repository.
    @Override
    public Match get(Integer id) {
        return matches.get(id);  // Return the match corresponding to the provided ID.
    }

    // Method to retrieve all matches stored in the repository.
    @Override
    public Set<Match> getAll() {
        return Set.copyOf(matches.values());  // Return a copy of all matches in the repository.
    }

    // Method to retrieve matches played on a specific date and time from the repository.
    @Override
    public Set<Match> getByPlayedOn(LocalDateTime playedOn) {
        return matches.values().stream()
                .filter(match -> match.getPlayedOn().equals(playedOn))
                .collect(Collectors.toSet());  // Return matches with the specified playedOn date and time.
    }

    // Method to retrieve matches played on a specific map from the repository.
    @Override
    public Set<Match> getByMapId(int mapId) {
        return matches.values().stream()
                .filter(match -> match.getMapId() == mapId)
                .collect(Collectors.toSet());  // Return matches played on the specified map.
    }
}
