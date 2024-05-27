package com.valorant.domain.jdbc.repositories;

import com.valorant.models.Map;
import com.valorant.models.MapImpl;
import com.valorant.repositories.MapRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * JDBC implementation of the {@link MapRepository} interface for managing maps in the Valorant game.
 */
public class JdbcMapRepository implements MapRepository {

    private static final String SELECT_ALL_MAPS = "SELECT * FROM map";
    private static final String SELECT_MAP_BY_ID = "SELECT * FROM map WHERE map_id = ?";
    private static final String SELECT_MAP_BY_NAME = "SELECT * FROM map WHERE name = ?";
    private static final String INSERT_MAP = "INSERT INTO map (name, type) VALUES (?, ?)";
    private static final String DELETE_MAP = "DELETE FROM map WHERE map_id = ?";
    private final Connection connection;

    /**
     * Constructs a new JdbcMapRepository with the given database connection.
     *
     * @param connection the database connection.
     */
    public JdbcMapRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves a map by its name.
     *
     * @param name the name of the map.
     * @return the map with the specified name, or null if not found.
     */
    @Override
    public Map getByName(String name) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_MAP_BY_NAME)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToMap(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching map by name: " + name, e);
        }
        return null;
    }

    /**
     * Saves a map to the database. If the map already exists (based on its ID), it updates the existing record.
     * If the map is new (ID is not set), it inserts a new record.
     *
     * @param map the map to be saved or updated.
     */
    @Override
    public void save(Map map) {
        if (map.getId() == 0) {
            // Map is new, perform insert operation
            insertMap(map);
        } else {
            // Map already exists, perform update operation
            updateMap(map);
        }
    }

    /**
     * Inserts a new map into the database.
     *
     * @param map the map to be inserted.
     */
    private void insertMap(Map map) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_MAP, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, map.getName());
            statement.setString(2, map.getType());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    map.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving map: " + map.getName(), e);
        }
    }

    /**
     * Updates an existing map in the database.
     *
     * @param map the map to be updated.
     */
    private void updateMap(Map map) {
        String UPDATE_MAP = "UPDATE map SET name = ?, type = ? WHERE map_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_MAP)) {
            statement.setString(1, map.getName());
            statement.setString(2, map.getType());
            statement.setInt(3, map.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating map: " + map.getId(), e);
        }
    }

    /**
     * Deletes an existing map from the database.
     *
     * @param map the map to be deleted.
     */
    @Override
    public void delete(Map map) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_MAP)) {
            statement.setInt(1, map.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting map: " + map.getId(), e);
        }
    }

    /**
     * Retrieves a map by its unique identifier.
     *
     * @param id the unique identifier of the map.
     * @return the map with the specified ID, or null if not found.
     */
    @Override
    public Map get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_MAP_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToMap(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching map by ID: " + id, e);
        }
        return null;
    }

    /**
     * Retrieves all maps from the database.
     *
     * @return a set of all maps.
     */
    @Override
    public Set<Map> getAll() {
        Set<Map> maps = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_MAPS)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    maps.add(mapResultSetToMap(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching all maps", e);
        }
        return maps;
    }

    /**
     * Helper method to map ResultSet to Map object.
     *
     * @param resultSet the ResultSet containing map data.
     * @return the mapped Map object.
     * @throws SQLException if a database access error occurs.
     */
    private Map mapResultSetToMap(ResultSet resultSet) throws SQLException {
        Map map = new MapImpl();
        map.setId(resultSet.getInt("map_id"));
        map.setName(resultSet.getString("name"));
        map.setType(resultSet.getString("type"));
        return map;
    }
}
