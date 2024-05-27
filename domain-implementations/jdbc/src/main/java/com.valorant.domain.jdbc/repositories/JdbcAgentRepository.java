package com.valorant.domain.jdbc.repositories;

import com.valorant.models.Agent;
import com.valorant.models.AgentImpl;
import com.valorant.repositories.AgentRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * JDBC implementation of the {@link AgentRepository} interface for managing agents in the Valorant game.
 */
public class JdbcAgentRepository implements AgentRepository {

    private static final String SELECT_ALL_AGENTS = "SELECT * FROM AGENT";
    private static final String SELECT_AGENT_BY_ID = "SELECT * FROM AGENT WHERE AGENT_ID = ?";
    private static final String SELECT_AGENT_BY_NAME = "SELECT * FROM AGENT WHERE NAME = ?";
    private static final String INSERT_AGENT = "INSERT INTO AGENT (NAME, DESCRIPTION, ROLE) VALUES (?, ?, ?)";
    private static final String DELETE_AGENT = "DELETE FROM AGENT WHERE AGENT_ID = ?";
    private static final String UPDATE_AGENT = "UPDATE AGENT SET NAME = ?, DESCRIPTION = ?, ROLE = ? WHERE AGENT_ID = ?";
    private final Connection connection;

    /**
     * Constructs a new JdbcAgentRepository with the given database connection.
     *
     * @param connection the database connection.
     */
    public JdbcAgentRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves an agent by its name.
     *
     * @param name the name of the agent.
     * @return the agent with the specified name, or null if not found.
     */
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

    /**
     * Saves a new agent to the database.
     *
     * @param agent the agent to be saved.
     */
    @Override
    public void save(Agent agent) {
        try {
            if (agent.getId() == 0) {
                // Insert a new agent
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
                }
            } else {
                // Update an existing agent
                try (PreparedStatement statement = connection.prepareStatement(UPDATE_AGENT)) {
                    statement.setString(1, agent.getName());
                    statement.setString(2, agent.getDescription());
                    statement.setString(3, agent.getRole());
                    statement.setInt(4, agent.getId());
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving/updating agent: " + agent.getName(), e);
        }
    }

    /**
     * Deletes an existing agent from the database.
     *
     * @param agent the agent to be deleted.
     */
    @Override
    public void delete(Agent agent) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_AGENT)) {
            statement.setInt(1, agent.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting agent: " + agent.getId(), e);
        }
    }

    /**
     * Retrieves an agent by its unique identifier.
     *
     * @param id the unique identifier of the agent.
     * @return the agent with the specified ID, or null if not found.
     */
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

    /**
     * Retrieves all agents from the database.
     *
     * @return a set of all agents.
     */
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

    /**
     * Helper method to map ResultSet to Agent object.
     *
     * @param resultSet the ResultSet containing agent data.
     * @return the mapped Agent object.
     * @throws SQLException if a database access error occurs.
     */
    private Agent mapResultSetToAgent(ResultSet resultSet) throws SQLException {
        Agent agent = new AgentImpl();
        agent.setId(resultSet.getInt("AGENT_ID"));
        agent.setName(resultSet.getString("NAME"));
        agent.setDescription(resultSet.getString("DESCRIPTION"));
        agent.setRole(resultSet.getString("ROLE"));
        return agent;
    }
}
