package com.valorant.file.repositories;

import com.valorant.models.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerRepository implements com.valorant.repositories.PlayerRepository {
    private final Map<Integer, Player> players = new HashMap<>();
    private final String dataPath;

    public PlayerRepository(String dataPath) {
        this.dataPath = dataPath;
        load();
    }

    protected void load() {
        Path path = Path.of(dataPath);
        try {
            if (Files.exists(path) && Files.size(path) > 0) {
                try (var inputStream = new ObjectInputStream(new FileInputStream(dataPath))) {
                    Object obj = inputStream.readObject();
                    if (obj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<Integer, Player> loadedPlayers = (Map<Integer, Player>) obj;
                        players.putAll(loadedPlayers);
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

    private void write() {
        try (var outputStream = new ObjectOutputStream(new FileOutputStream(dataPath))) {
            outputStream.writeObject(players);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Player player) {
        if (player.getId() <= 0) {
            int newId = players.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
            player.setId(newId);
        }
        players.put(player.getId(), player);
        write();
    }

    @Override
    public void delete(Player player) {
        players.remove(player.getId());
        write();
    }

    @Override
    public Player get(Integer id) {
        return players.get(id);
    }

    @Override
    public Set<Player> getAll() {
        return Set.copyOf(players.values());
    }

    @Override
    public Player getByUsername(String username) {
        return players.values().stream()
                .filter(player -> player.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<Player> getByRegion(String region) {
        return players.values().stream()
                .filter(player -> player.getRegion().equals(region))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Player> getByDisplayName(String displayName) {
        return players.values().stream()
                .filter(player -> player.getDisplayName().equals(displayName))
                .collect(Collectors.toSet());
    }
}
