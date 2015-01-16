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

    @PersistenceContext(unitName = "MeteoCalPU")
    private EntityManager em;

    @EJB
    private UserManager userManager;
    
    @EJB
    private UserFacade userFacade;

    @EJB
    private UserDAO userDAO;

    public void createUser(String uname) {
        User u = new User();
        u.setUsername(uname);
        u.setEmail("test@e.com");
        u.setPassword("pw");
        userManager.register(u);
    }

    public User getUser(String uname) {
        return userFacade.findByUsername(uname);
    }

    
    
}
