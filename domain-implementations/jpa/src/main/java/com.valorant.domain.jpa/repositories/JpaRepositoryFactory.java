package com.valorant.domain.jpa.repositories;

import jakarta.persistence.EntityManager;

public class JpaRepositoryFactory {

    private final EntityManager entityManager;

    public JpaRepositoryFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public JpaAgentRepository createAgentRepository() {
        return new JpaAgentRepository(entityManager);
    }

    public JpaMapRepository createMapRepository() {
        return new JpaMapRepository(entityManager);
    }

    public JpaMatchRepository createMatchRepository() {
        return new JpaMatchRepository(entityManager);
    }

    public JpaPlayerRepository createPlayerRepository() {
        return new JpaPlayerRepository(entityManager);
    }

    public JpaWeaponRepository createWeaponRepository() {
        return new JpaWeaponRepository(entityManager);
    }

    public JpaMatchPlayerRepository createMatchPlayerRepository() {
        return new JpaMatchPlayerRepository(entityManager, createMatchRepository());
    }

    public JpaPlayerAgentRepository createPlayerAgentRepository() {
        return new JpaPlayerAgentRepository(entityManager, createAgentRepository());
    }

    public JpaPlayerWeaponRepository createPlayerWeaponRepository() {
        return new JpaPlayerWeaponRepository(entityManager, createWeaponRepository());
    }
}
