/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.tests;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import com.meteocal.business.facade.UserFacade;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.validator.Validator;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    /**
     * Creates a new instance of TestParticipation
     */
    public void testCreateParticipation() {
        User u = new User();
        Event e = new Event();
        
        u.setUsername("up1");
        u.setPassword("pp1");
        u.setEmail("ep1");
        
        userFacade.save(u);
        System.out.println("[TESTCREATEPARTECIPATION] USER REGISTERED");
        
        try {
            e.setName("Test participation event");
            em.persist(e);
            System.out.println("[TESTCREATEPARTECIPATION] EVENT REGISTERED");
        } catch(ConstraintViolationException cve) {
            System.out.println("[TESTCREATEPARTECIPATION] EVENT - CONSTRAINT VIOLATION EXCEPTION");
            System.out.println(cve.getMessage());
        }
        
        try {
            u.partecipateTo(e);

            em.merge(u);
            em.flush();
            System.out.print("[TESTCREATEPARTECIPATION] PARTECIPATION REGISTERED");
        } catch(ConstraintViolationException cve) {
            System.out.println("[TESTCREATEPARTECIPATION] PARTECIPATION - CONSTRAINT VIOLATION EXCEPTION");
            System.out.println(cve.getMessage());
        } catch(Exception exception) {
            System.out.println("[TESTCREATEPARTECIPATION] PARTECIPATION - EXCEPTION");
            System.out.println(exception.getMessage());
        }
    }
}
