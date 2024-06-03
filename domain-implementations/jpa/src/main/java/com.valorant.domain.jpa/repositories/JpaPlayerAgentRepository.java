package com.valorant.domain.jpa.repositories;

import com.valorant.domain.jpa.models.AgentEntity;
import com.valorant.domain.jpa.models.JpaModelFactory;
import com.valorant.domain.jpa.models.PlayerEntity;
import com.valorant.models.Agent;
import com.valorant.models.Player;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class JpaPlayerAgentRepository {
    private final EntityManager entityManager;
    private final JpaAgentRepository agentRepository;

    public JpaPlayerAgentRepository(EntityManager entityManager, JpaAgentRepository agentRepository) {
        this.entityManager = entityManager;
        this.agentRepository = agentRepository;
    }

    public void assignAgentToPlayer(int playerId, int agentId) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            PlayerEntity player = entityManager.find(PlayerEntity.class, playerId);
            AgentEntity agent = entityManager.find(AgentEntity.class, agentId);
            if (player != null && agent != null) {
                player.setAgent(agent);
                entityManager.merge(player);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error assigning agent to player", e);
        }
    }

    public Agent getAgentByPlayerId(int playerId) {
        try {
            PlayerEntity player = entityManager.find(PlayerEntity.class, playerId);
            return player != null ? JpaModelFactory.toModel(player.getAgent()) : null;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving agent by player ID", e);
        }
    }
}
