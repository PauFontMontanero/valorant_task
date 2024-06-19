package com.valorant.domain.jpa.repositories;

import com.valorant.repositories.*;
import jakarta.persistence.EntityManager;

public class JpaRepositoryFactory implements RepositoryFactory {

    private final EntityManager entityManager;

    public JpaRepositoryFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public AgentRepository getAgentRepository() {
        return new JpaAgentRepository(entityManager);
    }

    @Override
    public MapRepository getMapRepository() {
        return new JpaMapRepository(entityManager);
    }

    @Override
    public MatchRepository getMatchRepository() {
        return new JpaMatchRepository(entityManager);
    }

    @Override
    public PlayerRepository getPlayerRepository() {
        return new JpaPlayerRepository(entityManager);
    }

    @Override
    public WeaponRepository getWeaponRepository() {
        return new JpaWeaponRepository(entityManager);
    }
}
