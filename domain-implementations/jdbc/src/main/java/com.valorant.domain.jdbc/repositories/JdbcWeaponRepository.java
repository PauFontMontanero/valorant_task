package com.valorant.domain.jdbc.repositories;

import com.valorant.models.Weapon;
import com.valorant.models.WeaponImpl;
import com.valorant.repositories.WeaponRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class JdbcWeaponRepository implements WeaponRepository {

    private static final String SELECT_ALL_WEAPONS = "SELECT * FROM weapon";
    private static final String SELECT_WEAPON_BY_ID = "SELECT * FROM weapon WHERE id = ?";
    private static final String INSERT_WEAPON = "INSERT INTO weapon (id, name, type) VALUES (?, ?, ?)";
    private static final String DELETE_WEAPON = "DELETE FROM weapon WHERE id = ?";
    private final Connection connection;

    public JdbcWeaponRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Weapon weapon) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_WEAPON)) {
            statement.setInt(1, weapon.getId());
            statement.setString(2, weapon.getName());
            statement.setString(3, weapon.getType());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving weapon: " + weapon.getId(), e);
        }
    }

    @Override
    public void delete(Weapon weapon) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_WEAPON)) {
            statement.setInt(1, weapon.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting weapon: " + weapon.getId(), e);
        }
    }

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
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String type = resultSet.getString("type");
        return new WeaponImpl(id, name, type);
    }
}
