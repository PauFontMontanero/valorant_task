// JdbcPlayerWeaponRepository.java
package com.valorant.domain.jdbc.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.valorant.models.Weapon;

public class JdbcPlayerWeaponRepository {

    private final Connection connection;
    private final JdbcWeaponRepository weaponRepository;

    public JdbcPlayerWeaponRepository(Connection connection, JdbcWeaponRepository weaponRepository) {
        this.connection = connection;
        this.weaponRepository = weaponRepository;
    }

    public void assignWeaponToPlayer(int playerId, int weaponId) throws SQLException {
        String sql = "UPDATE player_weapon SET weapon_id = ? WHERE player_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, weaponId);
            statement.setInt(2, playerId);
            statement.executeUpdate();
        }
    }

    public Weapon getWeaponByPlayerId(int playerId) throws SQLException {
        String sql = "SELECT weapon_id FROM player_weapon WHERE player_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int weaponId = resultSet.getInt("weapon_id");
                    // Assuming there's a method to retrieve a Weapon object by its ID
                    return weaponRepository.get(weaponId);
                }
            }
        }
        return null;
    }
}
