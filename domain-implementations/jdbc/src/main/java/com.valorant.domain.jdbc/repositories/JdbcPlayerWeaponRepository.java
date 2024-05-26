package com.valorant.domain.jdbc.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.valorant.models.Weapon;

/**
 * Handles database operations related to player weapons.
 */
public class JdbcPlayerWeaponRepository {

    private final Connection connection;
    private final JdbcWeaponRepository weaponRepository;

    /**
     * Initializes a JdbcPlayerWeaponRepository with a database connection and a Weapon repository.
     *
     * @param connection      The database connection.
     * @param weaponRepository The repository for Weapon objects.
     */
    public JdbcPlayerWeaponRepository(Connection connection, JdbcWeaponRepository weaponRepository) {
        this.connection = connection;
        this.weaponRepository = weaponRepository;
    }

    /**
     * Assigns a weapon to a player.
     *
     * @param playerId The ID of the player.
     * @param weaponId The ID of the weapon to assign.
     * @throws SQLException If an SQL exception occurs.
     */
    public void assignWeaponToPlayer(int playerId, int weaponId) throws SQLException {
        String sql = "INSERT INTO player_weapon (player_id, weapon_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE weapon_id = VALUES(weapon_id)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerId);
            statement.setInt(2, weaponId);
            statement.executeUpdate();
            System.out.println("Weapon with ID " + weaponId + " assigned to player with ID " + playerId);
        } catch (SQLException ex) {
            System.out.println("Error assigning weapon to player: " + ex.getMessage());
            throw ex; // Rethrow the exception for higher-level handling
        }
    }

    /**
     * Retrieves the weapon assigned to a player.
     *
     * @param playerId The ID of the player.
     * @return The weapon assigned to the player, or null if none is assigned.
     * @throws SQLException If an SQL exception occurs.
     */
    public Weapon getWeaponByPlayerId(int playerId) throws SQLException {
        String sql = "SELECT weapon_id FROM player_weapon WHERE player_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int weaponId = resultSet.getInt("weapon_id");
                    // Assuming there's a method to retrieve a Weapon object by its ID
                    Weapon weapon = weaponRepository.get(weaponId);
                    if (weapon == null) {
                        System.out.println("Weapon with ID " + weaponId + " not found.");
                    }
                    return weapon;
                } else {
                    System.out.println("No weapon assigned to player with ID " + playerId);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching weapon by player ID: " + ex.getMessage());
            throw ex; // Rethrow the exception for higher-level handling
        }
        return null; // Return null if no weapon found for the player
    }
}
