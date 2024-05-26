package com.valorant.domain.jdbc.repositories;

import com.valorant.models.Match;
import com.valorant.repositories.MatchRepository;
import com.valorant.models.MatchImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Repository class for managing matches in the Valorant game using JDBC.
 * This repository handles operations such as saving, deleting, and retrieving matches from the database.
 * Implements the {@link com.valorant.repositories.MatchRepository} interface.
 */
public class JdbcMatchRepository implements MatchRepository {

    private static final String SELECT_ALL_MATCHES = "SELECT * FROM `MATCH`";
    private static final String SELECT_MATCH_BY_ID = "SELECT * FROM `MATCH` WHERE MATCH_ID = ?";
    private static final String INSERT_MATCH = "INSERT INTO `MATCH` (PLAYED_ON, MAP_ID, OUTCOME) VALUES (?, ?, ?)";
    private static final String DELETE_MATCH = "DELETE FROM `MATCH` WHERE MATCH_ID = ?";
    private final Connection connection;

    /**
     * Constructs a new JdbcMatchRepository with the given database connection.
     *
     * @param connection the database connection.
     */
    public JdbcMatchRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Match match) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_MATCH, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, match.getPlayedOn());
            statement.setInt(2, match.getMapId());
            statement.setString(3, match.getOutcome());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    match.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating match failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving match: " + match.getId(), e);
        }
    }

    @Override
    public void delete(Match match) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_MATCH)) {
            statement.setInt(1, match.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting match: " + match.getId(), e);
        }
    }

    @Override
    public Match get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_MATCH_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToMatch(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching match by ID: " + id, e);
        }
        return null;
    }

    @Override
    public Set<Match> getAll() {
        Set<Match> matches = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_MATCHES)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    matches.add(mapResultSetToMatch(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching all matches", e);
        }
        return matches;
    }

    @Override
    public Set<Match> getByPlayedOn(LocalDateTime playedOn) {
        String SELECT_MATCHES_BY_PLAYED_ON = "SELECT * FROM `match` WHERE PLAYED_ON = ?";
        Set<Match> matches = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_MATCHES_BY_PLAYED_ON)) {
            statement.setObject(1, playedOn);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    matches.add(mapResultSetToMatch(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching matches by playedOn: " + playedOn, e);
        }
        return matches;
    }

    @Override
    public Set<Match> getByMapId(int mapId) {
        String SELECT_MATCHES_BY_MAP_ID = "SELECT * FROM `match` WHERE MAP_ID = ?";
        Set<Match> matches = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_MATCHES_BY_MAP_ID)) {
            statement.setInt(1, mapId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    matches.add(mapResultSetToMatch(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching matches by mapId: " + mapId, e);
        }
        return matches;
    }

    // Helper method to map ResultSet to Match object
    private Match mapResultSetToMatch(ResultSet resultSet) throws SQLException {
        Match match = new MatchImpl();
        match.setId(resultSet.getInt("MATCH_ID"));
        match.setPlayedOn(resultSet.getObject("PLAYED_ON", LocalDateTime.class));
        match.setMapId(resultSet.getInt("MAP_ID"));
        match.setOutcome(resultSet.getString("OUTCOME"));
        return match;
    }
}
