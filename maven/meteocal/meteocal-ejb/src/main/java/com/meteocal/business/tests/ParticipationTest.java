/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.tests;

import com.meteocal.business.dao.UserDAO;
import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import com.meteocal.business.facade.UserFacade;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.validator.Validator;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Andrea Bignoli
 */
@Stateless
public class ParticipationTest {

    @PersistenceContext(unitName = "MeteoCalPU")
    private EntityManager em;

    @EJB
    private UserFacade userFacade;

    @EJB
    private UserDAO userDAO;

    /**
     * Creates a new instance of TestParticipation
     */
    public void testCreateParticipation() {
        User u1 = new User();
        Event e = new Event();

        u1.setUsername("up1");
        u1.setPassword("pp1");
        u1.setEmail("ep1");

        userFacade.save(u1);

        User u2 = new User();

        u2.setUsername("up2");
        u2.setPassword("pp2");
        u2.setEmail("ep2");

        userFacade.save(u2);

        System.out.println("[TESTCREATEPARTECIPATION] USER REGISTERED");

        try {
            e.setName("ep1");
            e.setDescription("Test description");
            em.persist(e);
            System.out.println("[TESTCREATEPARTECIPATION] EVENT REGISTERED");
        } catch (ConstraintViolationException cve) {
            System.out.println("[TESTCREATEPARTECIPATION] EVENT - CONSTRAINT VIOLATION EXCEPTION");
            System.out.println(cve.getMessage());
            for (ConstraintViolation cv : cve.getConstraintViolations()) {
                System.out.println(cv.getMessage());
            }
        }

        try {
//            Doesn't work because User is not the owner of the relationship
//            u1.participateTo(e);
//            Works because Event is the owner of the relationship
            e.addParticipant(u2);

//            em.flush();
            System.out.print("[TESTCREATEPARTECIPATION] PARTECIPATION REGISTERED");
        } catch (ConstraintViolationException cve) {
            System.out.println("[TESTCREATEPARTECIPATION] PARTECIPATION - CONSTRAINT VIOLATION EXCEPTION");
            System.out.println(cve.getMessage());
            for (ConstraintViolation cv : cve.getConstraintViolations()) {
                System.out.println(cv.getMessage());
            }
        } catch (Exception exception) {
            System.out.println("[TESTCREATEPARTECIPATION] PARTECIPATION - EXCEPTION");
            System.out.println(exception.getMessage());
        }
    }
    
    public void testRemove() {
        User retrievedU2 = userDAO.findByUsername("up2");
        if(retrievedU2.getParticipatingTo().size() > 0)
        retrievedU2.removeParticipatingTo(retrievedU2.getParticipatingTo().get(0));
    }
    
    public void checkResults() {
        User retrievedU2 = userDAO.findByUsername("up2");
//        The refresh calls makes the difference in being able to access the events the user decided to participate to
//        This is probably related to the fact that the entity was previously managed in this persistence context
        em.refresh(retrievedU2);
        retrievedU2.getParticipatingTo();
        System.out.println("U2 is participating to " + retrievedU2.getParticipatingTo().size() + " events.");


        Query query = em.createQuery("select e from Event e where e.name = :eventName");
        query.setParameter("eventName", "ep1");
        Event retrievedE = (Event) query.getSingleResult();
        System.out.print("[TESTCREATEPARTECIPATION] SUCCESS -> END");
    }
}
