/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.dao;

import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Map.Entry;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Andrea Bignoli
 */
public abstract class DAObase<T> {

    private final static String UNIT_NAME = "MeteoCalPU";

    @PersistenceContext(unitName = UNIT_NAME)
    private EntityManager em;

    private String tableName;
    private Class<T> databaseEntityClass;

    public DAObase(Class<T> databaseEntityClass, String tableName) {
        this.databaseEntityClass = databaseEntityClass;
        this.tableName = tableName;
    }

    public void save(T entity) {
        try {
            em.persist(entity);
        } catch (Exception e) {
            System.out.println("[ERROR - PERSISTENCE] While running query: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public T update(T entity) {
        // This also persits the entity if it isn't already in the database.

        try {
            T result = em.merge(entity);
            em.flush();
            return result;

        } catch (Exception e) {
            System.out.println("[ERROR - PERSISTENCE] While running query: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public void delete(T entityToRemove) {
        try {
            T toRemove = em.merge(entityToRemove);
            em.remove(toRemove);
        } catch (Exception e) {
            System.out.println("[ERROR - PERSISTENCE] While running query: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void detach(T entityToDetach) {
        try {
            em.detach(entityToDetach);
        } catch (Exception e) {
            System.out.println("[ERROR - PERSISTENCE] While running query: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public T find(Object primaryKey) {
        try {
            return em.find(databaseEntityClass, primaryKey);
        } catch (Exception e) {
            System.out.println("[ERROR - PERSISTENCE] While running query: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public T retrieve(Object primaryKey) throws NotFoundException {
        T result = find(primaryKey);

        if (result == null) {
            throw new NotFoundException(NotFoundException.GENERIC_NOT_FOUND + " - Class: " + databaseEntityClass.getName() + " Key: " + primaryKey.toString());
        }

        return result;
    }

    public boolean exists(Object primaryKey) {
        T result = find(primaryKey);

        return result != null;
    }

    public List<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(databaseEntityClass);
        Root<T> rootEntry = cq.from(databaseEntityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all);

        return allQuery.getResultList();

    }

    protected T findSingleResult(String namedQuery, Map<String, Object> parameters) {
        T result = null;

        try {
            Query query = em.createQuery(namedQuery);

            // Method that will populate parameters if they are passed not null
            // and empty
            if (parameters != null && !parameters.isEmpty()) {
                insertQueryParameters(query, parameters);
            }
            try {
                result = (T) query.getSingleResult();
            } catch (NoResultException nre) {
                result = null;
            }

        } catch (Exception e) {
            System.out.println("Error while running query: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    private void insertQueryParameters(Query query, Map<String, Object> parameters) {
        for (Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }
}
