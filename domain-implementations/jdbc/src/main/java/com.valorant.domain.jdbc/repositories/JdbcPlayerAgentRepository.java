// JdbcPlayerAgentRepository.java
package com.valorant.domain.jdbc.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.valorant.models.Agent;

public class JdbcPlayerAgentRepository {

    private final Connection connection;
    private final JdbcAgentRepository agentRepository;

    public JdbcPlayerAgentRepository(Connection connection, JdbcAgentRepository agentRepository) {
        this.connection = connection;
        this.agentRepository = agentRepository;
    }

    public void assignAgentToPlayer(int playerId, int agentId) throws SQLException {
        String sql = "UPDATE player_agent SET agent_id = ? WHERE player_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, agentId);
            statement.setInt(2, playerId);
            statement.executeUpdate();
        }
    }

    public Agent getAgentByPlayerId(int playerId) throws SQLException {
        String sql = "SELECT agent_id FROM player_agent WHERE player_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int agentId = resultSet.getInt("agent_id");
                    // Assuming there's a method to retrieve an Agent object by its ID
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
