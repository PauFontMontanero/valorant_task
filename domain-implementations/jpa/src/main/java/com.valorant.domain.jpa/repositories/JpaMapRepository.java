package com.valorant.domain.jpa.repositories;

import com.valorant.domain.jpa.models.JpaModelFactory;
import com.valorant.domain.jpa.models.MapEntity;
import com.valorant.models.Map;
import com.valorant.repositories.MapRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.Set;
import java.util.stream.Collectors;

public class JpaMapRepository implements MapRepository {
    private final EntityManager entityManager;

    public JpaMapRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Map model) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            MapEntity entity = JpaModelFactory.toEntity(model);
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
    public void delete(Map model) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            MapEntity entity = JpaModelFactory.toEntity(model);
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    @Override
    public Map get(Integer id) {
        MapEntity entity = entityManager.find(MapEntity.class, id);
        return entity != null ? JpaModelFactory.toModel(entity) : null;
    }

    @Override
    public Set<Map> getAll() {
        try {
            return entityManager.createQuery("SELECT m FROM MapEntity m", MapEntity.class)
                    .getResultList()
                    .stream()
                    .map(JpaModelFactory::toModel)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all maps", e);
        }
    }

    @Override
    public Map getByName(String name) {
        try {
            MapEntity entity = entityManager.createQuery("SELECT m FROM MapEntity m WHERE m.name = :name", MapEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return JpaModelFactory.toModel(entity);
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving map by name", e);
        }
    }
}
