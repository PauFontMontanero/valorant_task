package com.valorant.domain.jpa.repositories;

import com.valorant.domain.jpa.models.JpaModelFactory;
import com.valorant.domain.jpa.models.MatchEntity;
import com.valorant.models.Match;
import com.valorant.repositories.MatchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaMatchRepository implements MatchRepository {
    private final EntityManager entityManager;

    public JpaMatchRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Match model) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            MatchEntity entity = JpaModelFactory.toEntity(model);
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
            throw new RuntimeException("Error saving match", e);
        }
    }

    @Override
    public void delete(Match model) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            MatchEntity entity = entityManager.find(MatchEntity.class, model.getId());
            if (entity != null) {
                entityManager.remove(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting match", e);
        }
    }

    @Override
    public Match get(Integer id) {
        MatchEntity entity = entityManager.find(MatchEntity.class, id);
        return entity != null ? JpaModelFactory.toModel(entity) : null;
    }

    @Override
    public Set<Match> getAll() {
        try {
            return entityManager.createQuery("SELECT m FROM MatchEntity m", MatchEntity.class)
                    .getResultList()
                    .stream()
                    .map(JpaModelFactory::toModel)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all matches", e);
        }
    }

    @Override
    public Set<Match> getByPlayedOn(LocalDateTime playedOn) {
        try {
            return entityManager.createQuery("SELECT m FROM MatchEntity m WHERE m.playedOn = :playedOn", MatchEntity.class)
                    .setParameter("playedOn", playedOn)
                    .getResultList()
                    .stream()
                    .map(JpaModelFactory::toModel)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving matches by playedOn", e);
        }
    }

    @Override
    public Set<Match> getByMapId(int mapId) {
        try {
            return entityManager.createQuery("SELECT m FROM MatchEntity m WHERE m.map.id = :mapId", MatchEntity.class)
                    .setParameter("mapId", mapId)
                    .getResultList()
                    .stream()
                    .map(JpaModelFactory::toModel)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving matches by mapId", e);
        }
    }
}
