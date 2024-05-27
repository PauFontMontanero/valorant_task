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

/**
 * JDBC implementation of PlayerRepository interface.
 * This class manages operations related to players in the Valorant game using JDBC.
 * Implements {@link PlayerRepository}.
 */
public class JdbcPlayerRepository implements PlayerRepository {

    private static final String SELECT_ALL_PLAYERS = "SELECT * FROM player";
    private static final String SELECT_PLAYER_BY_ID = "SELECT * FROM player WHERE PLAYER_ID = ?";
    private static final String INSERT_PLAYER = "INSERT INTO player (username, display_name, email, region, `rank`) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_PLAYER = "DELETE FROM player WHERE PLAYER_ID = ?";
    private static final String UPDATE_PLAYER = "UPDATE player SET username = ?, display_name = ?, email = ?, region = ?, `rank` = ? WHERE PLAYER_ID = ?";
    private final Connection connection;

    /**
     * Constructs a new JdbcPlayerRepository with the given database connection.
     *
     * @param connection The database connection.
     */
    public JdbcPlayerRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Saves a player to the database.
     *
     * @param player The player to save.
     * @throws RuntimeException If an error occurs while saving the player.
     */
    @Override
    public void save(Player player) {
        try {
            if (player.getId() == 0) {
                // Insert a new player
                try (PreparedStatement statement = connection.prepareStatement(INSERT_PLAYER, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, player.getUsername());
                    statement.setString(2, player.getDisplayName());
                    statement.setString(3, player.getEmail());
                    statement.setString(4, player.getRegion());
                    statement.setString(5, player.getRank());
                    statement.executeUpdate();
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        player.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Failed to retrieve auto-generated ID.");
                    }
                }
            } else {
                // Update an existing player
                try (PreparedStatement statement = connection.prepareStatement(UPDATE_PLAYER)) {
                    statement.setString(1, player.getUsername());
                    statement.setString(2, player.getDisplayName());
                    statement.setString(3, player.getEmail());
                    statement.setString(4, player.getRegion());
                    statement.setString(5, player.getRank());
                    statement.setInt(6, player.getId());
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving player: " + player.getUsername(), e);
        }
    }


    /**
     * Deletes a player from the database.
     *
     * @param player The player to delete.
     * @throws RuntimeException If an error occurs while deleting the player.
     */
    @Override
    public void delete(Player player) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_PLAYER)) {
            statement.setInt(1, player.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting player: " + player.getId(), e);
        }
    }

    /**
     * Retrieves a player by their ID from the database.
     *
     * @param id The ID of the player to retrieve.
     * @return The player object if found, null otherwise.
     * @throws RuntimeException If an error occurs while fetching the player.
     */
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

    /**
     * Retrieves a player by their username from the database.
     *
     * @param username The username of the player to retrieve.
     * @return The player object if found, null otherwise.
     * @throws RuntimeException If an error occurs while fetching the player.
     */
    @Override
    public Player getByUsername(String username) {
        String SELECT_PLAYER_BY_USERNAME = "SELECT * FROM player WHERE USERNAME = ?";
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

    /**
     * Retrieves a set of players by their region from the database.
     *
     * @param region The region of the players to retrieve.
     * @return A set of player objects.
     * @throws RuntimeException If an error occurs while fetching the players.
     */
    @Override
    public Set<Player> getByRegion(String region) {
        String SELECT_PLAYERS_BY_REGION = "SELECT * FROM player WHERE REGION = ?";
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

/**
 * Retrieves a set of players by their display name from the database.
 *
 * @param displayName The display name of the players to retrieve.
 * @return A set of player objects.
 * @throws RuntimeException If an error occurs while fetching the players
 */
@Override
public Set<Player> getByDisplayName(String displayName) {
    String SELECT_PLAYERS_BY_DISPLAY_NAME = "SELECT * FROM player WHERE DISPLAY_NAME = ?";
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

    /**
     * Retrieves all players from the database.
     *
     * @return A set of all player objects.
     * @throws RuntimeException If an error occurs while fetching the players.
     */
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
        player.setId(resultSet.getInt("PLAYER_ID"));
        player.setUsername(resultSet.getString("USERNAME"));
        player.setDisplayName(resultSet.getString("DISPLAY_NAME"));
        player.setEmail(resultSet.getString("EMAIL"));
        player.setRegion(resultSet.getString("REGION"));
        player.setRank(resultSet.getString("RANK"));
        return player;
    }
}