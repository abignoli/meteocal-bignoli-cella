/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.forecast;

import com.meteocal.business.dao.UserDAO;
import com.meteocal.business.entities.User;
import com.meteocal.business.entities.keys.InvitationID;
import com.meteocal.business.facade.UserFacade;
import com.meteocal.business.security.UserManager;
import com.meteocal.business.shared.security.PasswordEncrypter;
import javax.ejb.EJB;
import javax.validation.ConstraintViolationException;
import static org.hamcrest.CoreMatchers.is;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Andrea Bignoli
 */
@RunWith(Arquillian.class)
public class UseManagerIT {

    @EJB
    UserManager userManager;

    @EJB
    UserDAO userDAO;

    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackage(User.class.getPackage())
                .addPackage(UserManager.class.getPackage())
                .addPackage(UserFacade.class.getPackage())
                .addPackage(UserDAO.class.getPackage())
                .addPackage(InvitationID.class.getPackage())
                .addPackage(PasswordEncrypter.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml");
    }

    @Test
    public void newUsersShouldBeValid() {
        User newUser = new User();
        newUser.setUsername("test");
        newUser.setPassword("password");
        newUser.setEmail("invalidemail");
        try {
            userManager.register(newUser);
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof ConstraintViolationException);
        }

        assertNull(userDAO.find("test"));
    }
}
