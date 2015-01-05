/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Andrea Bignoli
 */
public abstract class DAObase<T> {
	private final static String UNIT_NAME = "MeteoCalPU";

	@PersistenceContext(unitName = UNIT_NAME)
	private EntityManager em;

	private Class<T> databaseEntityClass;

	public DAObase(Class<T> databaseEntityClass) {
            this.databaseEntityClass = databaseEntityClass;
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
			return em.merge(entity);
		} catch (Exception e) {
			System.out.println("[ERROR - PERSISTENCE] While running query: " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	public void delete(T entityToRemove) {
                // TODO merge may create a new entity on the database (if it's not already present). If an error occurs between merge and remove the new entity will be actually persisted on the db. FIX 
		try {
                    	T toRemove = em.merge(entityToRemove);
			em.remove(toRemove);
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
}
