/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.facade;

import business.dao.UserDAO;
import business.entities.User;
import business.exceptions.BusinessException;
import business.shared.data.Group;
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
            u.setGroupName(Group.USER.getName());
            u.encryptPassword();
            userDAO.save(u);
        }
        
        // TODO what if user not valid
    }

    public void update(User u) throws BusinessException {
        // Check if password has been modified, if this is the case the method will return since to modify the password the old one is required
        if(!checkPassword(u, u.getPassword()))
            throw new BusinessException(BusinessException.MISSING_PASSWORD);
        
        userDAO.update(u);
    }
    
    public void update(User u, String oldPassword) throws BusinessException {
        // Check if provided current password is correct
        if(!checkPassword(u, oldPassword))
            throw new BusinessException(BusinessException.WRONG_PASSWORD);
        
        userDAO.update(u);
    }

    public void remove(User u) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    private boolean checkPassword(User u, String password) {
        User userDBEntry = userDAO.find(u.getId());
        return userDBEntry.getPassword() == password;
    }
}
