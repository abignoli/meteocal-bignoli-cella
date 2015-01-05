/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.facade;

import business.dao.UserDAO;
import business.entities.User;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Andrea Bignoli
 */

@Stateless
public class UserFacadeImplementation implements UserFacade {
    @EJB
    private UserDAO userDAO;

    public void save(User u) {
        if(u.isValid()) {
            u.encryptPassword();
            userDAO.save(u);
        }
        
        // TODO what if user not valid
    }

    public User update(User u) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void delete(User u) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
