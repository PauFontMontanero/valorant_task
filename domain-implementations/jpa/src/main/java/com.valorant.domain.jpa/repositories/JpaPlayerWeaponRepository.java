package com.valorant.domain.jpa.repositories;

import com.valorant.domain.jpa.models.JpaModelFactory;
import com.valorant.domain.jpa.models.PlayerEntity;
import com.valorant.domain.jpa.models.WeaponEntity;
import com.valorant.models.Player;
import com.valorant.models.Weapon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class JpaPlayerWeaponRepository {
    private final EntityManager entityManager;
    private final JpaWeaponRepository weaponRepository;

    public JpaPlayerWeaponRepository(EntityManager entityManager, JpaWeaponRepository weaponRepository) {
        this.entityManager = entityManager;
        this.weaponRepository = weaponRepository;
    }

    public void assignWeaponToPlayer(int playerId, int weaponId) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            PlayerEntity player = entityManager.find(PlayerEntity.class, playerId);
            WeaponEntity weapon = entityManager.find(WeaponEntity.class, weaponId);
            if (player != null && weapon != null) {
                player.setWeapon(weapon);
                entityManager.merge(player);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error assigning weapon to player", e);
        }
    }

    public Weapon getWeaponByPlayerId(int playerId) {
        try {
            PlayerEntity player = entityManager.find(PlayerEntity.class, playerId);
            return player != null ? JpaModelFactory.toModel(player.getWeapon()) : null;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving weapon by player ID", e);
        }
    }
}
