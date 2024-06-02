package com.valorant.domain.jpa.repositories;

import com.valorant.domain.jpa.models.AgentEntity;
import com.valorant.domain.jpa.models.JpaModelFactory;
import com.valorant.models.Agent;
import com.valorant.repositories.AgentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaAgentRepository implements AgentRepository {
    private final EntityManager entityManager;

    public JpaAgentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Agent model) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            AgentEntity entity = JpaModelFactory.toEntity(model);
            if (model.getId() <= 0) {
                entityManager.persist(entity);
                model.setId(entity.getId());
            } else if (!entityManager.contains(entity)) {
                entityManager.merge(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public void delete(Agent model) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            AgentEntity entity = JpaModelFactory.toEntity(model);
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    @Override
    public Agent get(Integer id) {
        AgentEntity entity = entityManager.find(AgentEntity.class, id);
        return entity != null ? JpaModelFactory.toModel(entity) : null;
    }

    @Override
    public Set<Agent> getAll() {
        try {
            return entityManager.createQuery("SELECT a FROM AgentEntity a", AgentEntity.class)
                    .getResultList()
                    .stream()
                    .map(JpaModelFactory::toModel)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all agents", e);
        }
    }

    @Override
    public Agent getByName(String name) {
        try {
            List<AgentEntity> entities = entityManager.createQuery("SELECT a FROM AgentEntity a WHERE a.name = :name", AgentEntity.class)
                    .setParameter("name", name)
                    .getResultList();
            if (entities.isEmpty()) {
                return null;
            }
            return JpaModelFactory.toModel(entities.get(0)); // Or handle multiple results as needed
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving agent by name", e);
        }
    }
}
