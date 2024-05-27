package com.valorant.domain.jdbc.repositories;

import com.valorant.models.Weapon;
import com.valorant.models.WeaponImpl;
import com.valorant.repositories.WeaponRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * JDBC implementation of the WeaponRepository interface.
 * Implements {@link WeaponRepository}.
 */
public class JdbcWeaponRepository implements WeaponRepository {

    private static final String SELECT_ALL_WEAPONS = "SELECT WEAPON_ID, NAME, TYPE FROM weapon";
    private static final String SELECT_WEAPON_BY_ID = "SELECT * FROM weapon WHERE WEAPON_ID = ?";
    private static final String DELETE_WEAPON = "DELETE FROM weapon WHERE WEAPON_ID = ?";
    private static final String UPDATE_WEAPON = "UPDATE weapon SET NAME = ?, TYPE = ? WHERE WEAPON_ID = ?";
    private final Connection connection;

    /**
     * Constructs a new JdbcWeaponRepository with the given database connection.
     *
     * @param connection The database connection.
     */
    public JdbcWeaponRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Saves a weapon to the database.
     *
     * @param weapon The weapon to save.
     */
    public void save(Weapon weapon) {
        String sql = "INSERT INTO weapon (name, type) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, weapon.getName());
            statement.setString(2, weapon.getType());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Saving weapon failed, no rows affected.");
            }

            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    weapon.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Saving weapon failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving weapon: " + weapon.getName(), e);
        }
    }

    /**
     * Updates a weapon in the database.
     *
     * @param weapon The weapon to update.
     */
    public void update(Weapon weapon) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_WEAPON)) {
            statement.setString(1, weapon.getName());
            statement.setString(2, weapon.getType());
            statement.setInt(3, weapon.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating weapon: " + weapon.getName(), e);
        }
    }

    /**
     * Deletes a weapon from the database.
     *
     * @param weapon The weapon to delete.
     */
    @Override
    public void delete(Weapon weapon) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_WEAPON)) {
            statement.setInt(1, weapon.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting weapon: " + weapon.getId(), e);
        }
    }

    /**
     * Retrieves a weapon from the database by its ID.
     *
     * @param id The ID of the weapon to retrieve.
     * @return The retrieved weapon, or null if not found.
     */
    @Override
    public Weapon get(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_WEAPON_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToWeapon(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching weapon by ID: " + id, e);
        }
        return null;
    }

    /**
     * Retrieves a weapon from the database by its name.
     *
     * @param name The name of the weapon to retrieve.
     * @return The retrieved weapon, or null if not found.
     */
    @Override
    public Weapon getByName(String name) {
        String SELECT_WEAPON_BY_NAME = "SELECT * FROM weapon WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(SELECT_WEAPON_BY_NAME)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToWeapon(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching weapon by name: " + name, e);
        }
        return null;
    }

    /**
     * Retrieves all weapons from the database.
     *
     * @return A set of all weapons.
     */
    @Override
    public Set<Weapon> getAll() {
        Set<Weapon> weapons = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_WEAPONS)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    weapons.add(mapResultSetToWeapon(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching all weapons", e);
        }
        return weapons;
    }

    // Helper method to map ResultSet to Weapon object
    private Weapon mapResultSetToWeapon(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("WEAPON_ID");
        String name = resultSet.getString("NAME");
        String type = resultSet.getString("TYPE");
        return new WeaponImpl(id, name, type);
    }
}
