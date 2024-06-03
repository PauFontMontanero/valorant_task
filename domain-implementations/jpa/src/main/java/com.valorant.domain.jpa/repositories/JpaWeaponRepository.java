package com.valorant.domain.jpa.repositories;

import com.valorant.domain.jpa.models.JpaModelFactory;
import com.valorant.domain.jpa.models.WeaponEntity;
import com.valorant.models.Weapon;
import com.valorant.repositories.WeaponRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.Set;
import java.util.stream.Collectors;

public class JpaWeaponRepository implements WeaponRepository {
    private final EntityManager entityManager;

    public JpaWeaponRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Weapon model) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }
            WeaponEntity entity = JpaModelFactory.toEntity(model);
            if (model.getId() <= 0) {
                entityManager.persist(entity);
                model.setId(entity.getId());
            } else {
                entity = entityManager.merge(entity);
                model.setId(entity.getId());
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving weapon", e);
        }
    }

    @Override
    public void delete(Weapon model) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }
            WeaponEntity entity = entityManager.find(WeaponEntity.class, model.getId());
            if (entity != null) {
                entityManager.remove(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting weapon", e);
        }
    }

    @Override
    public Weapon get(Integer id) {
        WeaponEntity entity = entityManager.find(WeaponEntity.class, id);
        return entity != null ? JpaModelFactory.toModel(entity) : null;
    }

    @Override
    public Set<Weapon> getAll() {
        try {
            return entityManager.createQuery("SELECT w FROM WeaponEntity w", WeaponEntity.class)
                    .getResultList()
                    .stream()
                    .map(JpaModelFactory::toModel)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all weapons", e);
        }
    }

    @Override
    public Weapon getByName(String name) {
        try {
            TypedQuery<WeaponEntity> query = entityManager.createQuery("SELECT w FROM WeaponEntity w WHERE w.name = :name", WeaponEntity.class);
            query.setParameter("name", name);
            query.setMaxResults(1);
            WeaponEntity entity = query.getSingleResult();
            return JpaModelFactory.toModel(entity);
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving weapon by name", e);
        }
    }
}
