package com.valorant.domain.jdbc.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.valorant.models.Match;

/**
 * Repository class for managing the association between players and matches in the Valorant game using JDBC.
 * This repository handles operations such as adding a player to a match and retrieving matches by player ID.
 */
public class JdbcMatchPlayerRepository {

    private final Connection connection;
    private final JdbcMatchRepository matchRepository;

    /**
     * Constructs a new JdbcMatchPlayerRepository with the given database connection and match repository.
     *
     * @param connection      the database connection.
     * @param matchRepository the repository for matches.
     */
    public JdbcMatchPlayerRepository(Connection connection, JdbcMatchRepository matchRepository) {
        this.connection = connection;
        this.matchRepository = matchRepository;
    }

    /**
     * Adds a player to a match by inserting a record into the match_player table.
     *
     * @param playerId the ID of the player to be added.
     * @param matchId  the ID of the match to which the player is added.
     * @throws SQLException if a database access error occurs.
     */
    public void addPlayerToMatch(int playerId, int matchId) throws SQLException {
        String sql = "INSERT INTO match_player (player_id, match_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerId);
            statement.setInt(2, matchId);
            statement.executeUpdate();
        }
    }

    /**
     * Retrieves matches associated with a player by querying the match_player table.
     *
     * @param playerId the ID of the player.
     * @return a set of matches associated with the player.
     * @throws SQLException if a database access error occurs.
     */
    public Set<Match> getMatchesByPlayerId(int playerId) throws SQLException {
        Set<Match> matches = new HashSet<>();
        String sql = "SELECT * FROM match_player WHERE player_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int matchId = resultSet.getInt("match_id");
                    // Retrieve the match object from the match repository using its ID
                    Match match = matchRepository.get(matchId);
                    matches.add(match);
                }
            }
        }
        return matches;
    }
}
