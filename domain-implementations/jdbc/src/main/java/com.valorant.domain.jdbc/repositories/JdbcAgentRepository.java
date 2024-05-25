package com.valorant.domain.jdbc.repositories;

import com.valorant.models.Agent;
import com.valorant.models.AgentImpl;
import com.valorant.repositories.AgentRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcAgentRepository implements AgentRepository {

    private static final String SELECT_ALL_AGENTS = "SELECT * FROM AGENT";
    private static final String SELECT_AGENT_BY_ID = "SELECT * FROM AGENT WHERE AGENT_ID = ?";
    private static final String SELECT_AGENT_BY_NAME = "SELECT * FROM AGENT WHERE NAME = ?";
    private static final String INSERT_AGENT = "INSERT INTO AGENT (NAME, DESCRIPTION, ROLE) VALUES (?, ?, ?)";
    private static final String DELETE_AGENT = "DELETE FROM AGENT WHERE AGENT_ID = ?";
    private final Connection connection;

    public JdbcAgentRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Agent getByName(String name) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_AGENT_BY_NAME)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToAgent(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching agent by name: " + name, e);
        }
        return null;
    }

    @Override
    public void save(Agent agent) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_AGENT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, agent.getName());
            statement.setString(2, agent.getDescription());
            statement.setString(3, agent.getRole());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                agent.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Failed to retrieve auto-generated ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving agent: " + agent.getName(), e);
        }
    }

    @Override
    public void delete(Agent agent) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_AGENT)) {
            statement.setInt(1, agent.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting agent: " + agent.getId(), e);
        }
    }

    @Override
    public Agent get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_AGENT_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToAgent(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching agent by ID: " + id, e);
        }
        return null;
    }

    @Override
    public Set<Agent> getAll() {
        Set<Agent> agents = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_AGENTS)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    agents.add(mapResultSetToAgent(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching all agents", e);
        }
        return agents;
    }

    // Helper method to map ResultSet to Agent object
    private Agent mapResultSetToAgent(ResultSet resultSet) throws SQLException {
        Agent agent = new AgentImpl();
        agent.setId(resultSet.getInt("AGENT_ID"));
        agent.setName(resultSet.getString("NAME"));
        agent.setDescription(resultSet.getString("DESCRIPTION"));
        agent.setRole(resultSet.getString("ROLE"));
        return agent;
    }
}
