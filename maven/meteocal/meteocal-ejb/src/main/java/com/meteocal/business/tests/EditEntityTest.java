/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.tests;

import com.meteocal.business.dao.UserDAO;
import com.meteocal.business.entities.User;
import com.meteocal.business.facade.UserFacade;
import com.meteocal.business.security.UserManager;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ate
 */
@Stateless
public class EditEntityTest {
    
    private final String UNAME = "uname";

    @PersistenceContext(unitName = "MeteoCalPU")
    private EntityManager em;

    @EJB
    private UserManager userManager;
    
    @EJB
    private UserFacade userFacade;

    @EJB
    private UserDAO userDAO;

    public String createUser() {
        User u = new User();
        u.setUsername(UNAME);
        u.setEmail("test@e.com");
        u.setPassword("pw");
        userManager.register(u);
        
        return u.getUsername();
    }

    public User getUser(String uname) {
        return userFacade.findByUsername(uname);
    }
    
    public void mergeTest(User u) {
        System.out.println(em.contains(u) ? "The entity passed is managed!" : "The entity passed is NOT managed!");
        
        User retrievedU = getUser(UNAME);
        System.out.println(em.contains(retrievedU) ? "The entity retrieved is managed!" : "The entity passed is NOT managed!");
        
        User secondRetrievedU = getUser(UNAME);
        System.out.println(em.contains(retrievedU) ? "The second entity retrieved is managed!" : "The entity passed is NOT managed!");
        
        System.out.println(retrievedU == secondRetrievedU ? "The two retrieved entities are actually the same reference" : "The two retrieved entities are NOT the same reference");
        
        System.out.println(retrievedU == u ? "The retrieved entity and the one passed from the managed bean are actually the same reference" : "The retrieved entity and the one passed from the managed bean are NOT the same reference");
        
        em.merge(u);
    }
}
