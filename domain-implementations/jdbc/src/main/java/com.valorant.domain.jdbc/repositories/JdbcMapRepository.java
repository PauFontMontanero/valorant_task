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

public class JdbcMapRepository implements MapRepository {

    private static final String SELECT_ALL_MAPS = "SELECT * FROM map";
    private static final String SELECT_MAP_BY_ID = "SELECT * FROM map WHERE map_id = ?";
    private static final String SELECT_MAP_BY_NAME = "SELECT * FROM map WHERE name = ?";
    private static final String INSERT_MAP = "INSERT INTO map (name, type) VALUES (?, ?)";
    private static final String DELETE_MAP = "DELETE FROM map WHERE map_id = ?";
    private final Connection connection;

    public JdbcMapRepository(Connection connection) {
        this.connection = connection;
    }

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

    @Override
    public void save(Map map) {
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

    @Override
    public void delete(Map map) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_MAP)) {
            statement.setInt(1, map.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting map: " + map.getId(), e);
        }
    }

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

    // Helper method to map ResultSet to Map object
    private Map mapResultSetToMap(ResultSet resultSet) throws SQLException {
        Map map = new MapImpl();
        map.setId(resultSet.getInt("map_id"));
        map.setName(resultSet.getString("name"));
        map.setType(resultSet.getString("type"));
        return map;
    }
}
