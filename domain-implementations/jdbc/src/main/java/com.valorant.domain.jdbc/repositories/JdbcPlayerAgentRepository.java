// JdbcPlayerAgentRepository.java
package com.valorant.domain.jdbc.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.valorant.models.Agent;

/**
 * Repository class for managing the association between players and agents in the Valorant game using JDBC.
 * This repository handles operations such as assigning agents to players and retrieving agents assigned to players.
 */
public class JdbcPlayerAgentRepository {

    private final Connection connection;
    private final JdbcAgentRepository agentRepository;

    /**
     * Constructs a new JdbcPlayerAgentRepository with the given database connection and agent repository.
     *
     * @param connection     The database connection.
     * @param agentRepository The repository for accessing agent data.
     */
    public JdbcPlayerAgentRepository(Connection connection, JdbcAgentRepository agentRepository) {
        this.connection = connection;
        this.agentRepository = agentRepository;
    }

    /**
     * Assigns an agent to a player by inserting a record into the player_agent table.
     * If a record already exists for the player, it updates the agent_id.
     *
     * @param playerId The ID of the player.
     * @param agentId  The ID of the agent to assign.
     * @throws SQLException if a database error occurs.
     */
    public void assignAgentToPlayer(int playerId, int agentId) throws SQLException {
        String sql = "INSERT INTO player_agent (player_id, agent_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE agent_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerId);
            statement.setInt(2, agentId);
            statement.setInt(3, agentId);
            statement.executeUpdate();
        }
    }

    /**
     * Retrieves the agent assigned to a player with the given ID.
     *
     * @param playerId The ID of the player.
     * @return The agent assigned to the player, or null if no agent is assigned.
     * @throws SQLException if a database error occurs.
     */
    public Agent getAgentByPlayerId(int playerId) throws SQLException {
        String sql = "SELECT agent_id FROM player_agent WHERE player_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int agentId = resultSet.getInt("agent_id");
                    // Retrieve the agent object by its ID using the agentRepository
                    Agent agent = agentRepository.get(agentId);
                    if (agent == null) {
                        System.out.println("Agent with ID " + agentId + " not found.");
                    }
                    return agent;
                } else {
                    System.out.println("No agent assigned to player with ID " + playerId);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching agent by player ID: " + ex.getMessage());
            throw ex; // Rethrow the exception for higher-level handling
        }
        return null; // Return null if no agent found for the player
    }
}
