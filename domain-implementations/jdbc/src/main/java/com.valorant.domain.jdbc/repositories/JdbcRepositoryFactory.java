package com.valorant.domain.jdbc.repositories;

import com.valorant.exceptions.RepositoryException;
import com.valorant.repositories.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcRepositoryFactory implements RepositoryFactory {
    private final Connection connection;

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

    @Override
    public AgentRepository getAgentRepository() {
        return new JdbcAgentRepository(connection);
    }

    @Override
    public MapRepository getMapRepository() {
        return new JdbcMapRepository(connection);
    }

    @Override
    public MatchRepository getMatchRepository() {
        return new JdbcMatchRepository(connection);
    }

    @Override
    public PlayerRepository getPlayerRepository() {
        return new JdbcPlayerRepository(connection);
    }

    @Override
    public WeaponRepository getWeaponRepository() {
        return new JdbcWeaponRepository(connection);
    }
}
