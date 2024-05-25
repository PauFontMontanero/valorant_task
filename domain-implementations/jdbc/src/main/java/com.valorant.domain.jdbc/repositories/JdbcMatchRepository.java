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

public class JdbcMatchRepository implements MatchRepository {

    private static final String SELECT_ALL_MATCHES = "SELECT * FROM match";
    private static final String SELECT_MATCH_BY_ID = "SELECT * FROM match WHERE id = ?";
    private static final String INSERT_MATCH = "INSERT INTO match (id, played_on, map_id, outcome) VALUES (?, ?, ?, ?)";
    private static final String DELETE_MATCH = "DELETE FROM match WHERE id = ?";
    private final Connection connection;

    public JdbcMatchRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Match match) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_MATCH)) {
            statement.setInt(1, match.getId());
            statement.setObject(2, match.getPlayedOn());
            statement.setInt(3, match.getMapId());
            statement.setString(4, match.getOutcome());
            statement.executeUpdate();
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
        String SELECT_MATCHES_BY_PLAYED_ON = "SELECT * FROM match WHERE played_on = ?";
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
        String SELECT_MATCHES_BY_MAP_ID = "SELECT * FROM match WHERE map_id = ?";
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
        Match match = new MatchImpl(); // Use MatchImpl instead of an anonymous implementation
        match.setId(resultSet.getInt("id"));
        match.setPlayedOn(resultSet.getObject("played_on", LocalDateTime.class));
        match.setMapId(resultSet.getInt("map_id"));
        match.setOutcome(resultSet.getString("outcome"));
        return match;
    }

}
