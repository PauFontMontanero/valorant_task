package com.valorant.domain.jdbc.repositories;

import com.valorant.models.Player;
import com.valorant.models.PlayerImpl;
import com.valorant.repositories.PlayerRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class JdbcPlayerRepository implements PlayerRepository {

    private static final String SELECT_ALL_PLAYERS = "SELECT * FROM player";
    private static final String SELECT_PLAYER_BY_ID = "SELECT * FROM player WHERE id = ?";
    private static final String INSERT_PLAYER = "INSERT INTO player (id, username, display_name, email, region, rank) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String DELETE_PLAYER = "DELETE FROM player WHERE id = ?";
    private final Connection connection;

    public JdbcPlayerRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Player player) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_PLAYER)) {
            statement.setInt(1, player.getId());
            statement.setString(2, player.getUsername());
            statement.setString(3, player.getDisplayName());
            statement.setString(4, player.getEmail());
            statement.setString(5, player.getRegion());
            statement.setString(6, player.getRank());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving player: " + player.getId(), e);
        }
    }

    @Override
    public void delete(Player player) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_PLAYER)) {
            statement.setInt(1, player.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting player: " + player.getId(), e);
        }
    }

    @Override
    public Player get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPlayer(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching player by ID: " + id, e);
        }
        return null;
    }

    @Override
    public Player getByUsername(String username) {
        String SELECT_PLAYER_BY_USERNAME = "SELECT * FROM player WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER_BY_USERNAME)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPlayer(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching player by username: " + username, e);
        }
        return null;
    }

    @Override
    public Set<Player> getByRegion(String region) {
        String SELECT_PLAYERS_BY_REGION = "SELECT * FROM player WHERE region = ?";
        Set<Player> players = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_PLAYERS_BY_REGION)) {
            statement.setString(1, region);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    players.add(mapResultSetToPlayer(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching players by region: " + region, e);
        }
        return players;
    }

    @Override
    public Set<Player> getByDisplayName(String displayName) {
        String SELECT_PLAYERS_BY_DISPLAY_NAME = "SELECT * FROM player WHERE display_name = ?";
        Set<Player> players = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_PLAYERS_BY_DISPLAY_NAME)) {
            statement.setString(1, displayName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    players.add(mapResultSetToPlayer(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching players by display name: " + displayName, e);
        }
        return players;
    }

    @Override
    public Set<Player> getAll() {
        Set<Player> players = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PLAYERS)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    players.add(mapResultSetToPlayer(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching all players", e);
        }
        return players;
    }

    // Helper method to map ResultSet to Player object
    private Player mapResultSetToPlayer(ResultSet resultSet) throws SQLException {
        Player player = new PlayerImpl();
        player.setId(resultSet.getInt("id"));
        player.setUsername(resultSet.getString("username"));
        player.setDisplayName(resultSet.getString("display_name"));
        player.setEmail(resultSet.getString("email"));
        player.setRegion(resultSet.getString("region"));
        player.setRank(resultSet.getString("rank"));
        return player;
    }

}
