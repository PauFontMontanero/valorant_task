package com.valorant.domain.jpa.repositories;

import com.valorant.domain.jpa.models.JpaModelFactory;
import com.valorant.domain.jpa.models.PlayerEntity;
import com.valorant.models.Player;
import com.valorant.repositories.PlayerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.Set;
import java.util.stream.Collectors;

public class JpaPlayerRepository implements PlayerRepository {
    private final EntityManager entityManager;

    public JpaPlayerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Player model) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            PlayerEntity entity = JpaModelFactory.toEntity(model);
            if (model.getId() <= 0) {
                entityManager.persist(entity);
                model.setId(entity.getId());
            } else {
                entity = entityManager.merge(entity);
                model.setId(entity.getId());
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error saving player", e);
        }
    }

    @Override
    public void delete(Player model) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            PlayerEntity entity = entityManager.find(PlayerEntity.class, model.getId());
            if (entity != null) {
                entityManager.remove(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting player", e);
        }
    }

    @Override
    public Player get(Integer id) {
        PlayerEntity entity = entityManager.find(PlayerEntity.class, id);
        if (entity != null) {
            entity.getMatches().size(); // Initialize matches collection if it's lazy
        }
        return entity != null ? JpaModelFactory.toModel(entity) : null;
    }

    @Override
    public Set<Player> getAll() {
        try {
            return entityManager.createQuery("SELECT p FROM PlayerEntity p", PlayerEntity.class)
                    .getResultList()
                    .stream()
                    .map(JpaModelFactory::toModel)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all players", e);
        }
    }

    @Override
    public Player getByUsername(String username) {
        try {
            TypedQuery<PlayerEntity> query = entityManager.createQuery("SELECT p FROM PlayerEntity p WHERE p.username = :username", PlayerEntity.class);
            query.setParameter("username", username);
            query.setMaxResults(1);
            PlayerEntity entity = query.getSingleResult();
            return JpaModelFactory.toModel(entity);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Set<Player> getByRegion(String region) {
        try {
            return entityManager.createQuery("SELECT p FROM PlayerEntity p WHERE p.region = :region", PlayerEntity.class)
                    .setParameter("region", region)
                    .getResultList()
                    .stream()
                    .map(JpaModelFactory::toModel)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving players by region", e);
        }
    }

    @Override
    public Set<Player> getByDisplayName(String displayName) {
        try {
            return entityManager.createQuery("SELECT p FROM PlayerEntity p WHERE p.displayName = :displayName", PlayerEntity.class)
                    .setParameter("displayName", displayName)
                    .getResultList()
                    .stream()
                    .map(JpaModelFactory::toModel)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving players by displayName", e);
        }
    }
}
