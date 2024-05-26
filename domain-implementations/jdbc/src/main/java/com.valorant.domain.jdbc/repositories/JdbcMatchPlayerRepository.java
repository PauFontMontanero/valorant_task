// JdbcMatchPlayerRepository.java
package com.valorant.domain.jdbc.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.valorant.models.Match;

public class JdbcMatchPlayerRepository {

    private final Connection connection;
    private final JdbcMatchRepository matchRepository;

    public JdbcMatchPlayerRepository(Connection connection, JdbcMatchRepository matchRepository) {
        this.connection = connection;
        this.matchRepository = matchRepository;
    }

    public void addPlayerToMatch(int playerId, int matchId) throws SQLException {
        String sql = "INSERT INTO match_player (player_id, match_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerId);
            statement.setInt(2, matchId);
            statement.executeUpdate();
        }
    }

    public Set<Match> getMatchesByPlayerId(int playerId) throws SQLException {
        Set<Match> matches = new HashSet<>();
        String sql = "SELECT * FROM match_player WHERE player_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int matchId = resultSet.getInt("match_id");
                    // Assuming there's a method to retrieve a Match object by its ID
                    Match match = matchRepository.get(matchId);
                    matches.add(match);
                }
            }
        }
        return matches;
    }
}
