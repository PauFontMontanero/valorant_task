package com.valorant.domain.jdbc.repositories;

import com.valorant.exceptions.RepositoryException;
import com.valorant.repositories.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Factory class for JDBC repositories.
 * Implements {@link RepositoryFactory}.
 */
public class JdbcRepositoryFactory implements RepositoryFactory {

    private final Connection connection;

    /**
     * Initializes a new JdbcRepositoryFactory instance.
     */
    public JdbcRepositoryFactory() {
        try {
            var properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/datasource.properties"));

            connection = DriverManager.getConnection(String.format("%s:%s://%s/%s",
                            properties.getProperty("protocol"),
                            properties.getProperty("subprotocol"),
                            properties.getProperty("url"),
                            properties.getProperty("database")),
                    properties.getProperty("user"),
                    properties.getProperty("password"));
        } catch (SQLException e) {
            throw new RepositoryException(e);
        } catch (IOException e) {
            throw new RepositoryException();
        }
    }

    /**
     * Retrieves the Agent repository.
     *
     * @return The Agent repository.
     */
    @Override
    public AgentRepository getAgentRepository() {
        return new JdbcAgentRepository(connection);
    }

    /**
     * Retrieves the Map repository.
     *
     * @return The Map repository.
     */
    @Override
    public MapRepository getMapRepository() {
        return new JdbcMapRepository(connection);
    }

    /**
     * Retrieves the Match repository.
     *
     * @return The Match repository.
     */
    @Override
    public MatchRepository getMatchRepository() {
        return new JdbcMatchRepository(connection);
    }

    /**
     * Retrieves the Player repository.
     *
     * @return The Player repository.
     */
    @Override
    public PlayerRepository getPlayerRepository() {
        return new JdbcPlayerRepository(connection);
    }

    /**
     * Retrieves the Weapon repository.
     *
     * @return The Weapon repository.
     */
    @Override
    public WeaponRepository getWeaponRepository() {
        return new JdbcWeaponRepository(connection);
    }
}
