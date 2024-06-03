package com.valorant.domain.jpa.repositories;

import com.valorant.domain.jpa.models.MatchEntity;
import com.valorant.domain.jpa.models.PlayerEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Set;

public class JpaMatchPlayerRepository {
    private final EntityManager entityManager;
    private final JpaMatchRepository matchRepository;

    public JpaMatchPlayerRepository(EntityManager entityManager, JpaMatchRepository matchRepository) {
        this.entityManager = entityManager;
        this.matchRepository = matchRepository;
    }

    public void addPlayerToMatch(int playerId, int matchId) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            MatchEntity match = entityManager.find(MatchEntity.class, matchId);
            PlayerEntity player = entityManager.find(PlayerEntity.class, playerId);
            if (match != null && player != null) {
                match.getPlayers().add(player);
                player.getMatches().add(match); // Ensure bi-directional consistency
                entityManager.merge(match);
                entityManager.merge(player); // Merge player as well to ensure changes are reflected
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error adding player to match", e);
        }
    }

    public Set<MatchEntity> getMatchesByPlayerId(int playerId) {
        try {
            PlayerEntity player = entityManager.find(PlayerEntity.class, playerId);
            return player != null ? player.getMatches() : null;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving matches by player ID", e);
        }
    }
}
