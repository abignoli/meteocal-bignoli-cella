/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.dao.UserDAO;
import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.shared.data.Group;
import com.meteocal.business.shared.security.UserUserVisibility;
import java.util.ArrayList;
import java.util.List;
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
            u.setAndEncryptPassword(u.getPassword());
            userDAO.save(u);
        }
        
        // TODO what if user not valid
    }

    /**
     * Updates user data for the database entry identified by the ID of the user u.
     * The updated fields are:
     * 
     * Email
     * Calendar visibility
     * 
     * Any other field is ignored.
     * 
     * @param u 
     * The entity containing the updated data, as well as the ID of the entity to update
     * @throws BusinessException 
     */
    public void updateData(User u) throws NotFoundException {
        
        User dbEntry = userDAO.retrieve(u.getId());
        setUserData(dbEntry, u);
        
//        userDAO.update(u);
    }
    
    /**
     * Updates user data for the database entry identified by the ID of the user u.
     * The updated fields are:
     * 
     * Email
     * Password
     * Calendar visibility
     * 
     * Any other field is ignored.
     * 
     * @param u 
     * The entity containing the updated data, as well as the ID of the entity to update
     * @param oldPassword
     * @throws BusinessException 
     */
    public void updateData(User u, String oldPassword) throws BusinessException {
        // Check if provided current password is correct
        if(!checkPassword(u.getId(), oldPassword))
            throw new BusinessException(BusinessException.WRONG_PASSWORD);
        
        User dbEntry = userDAO.retrieve(u.getId());
        setUserData(dbEntry, u);
        dbEntry.setAndEncryptPassword(u.getPassword());
        
//        userDAO.update(u);
    }
    
    /**
     * Sets user data for the managed entity dbEntry using data provided by updated.
     * The updated fields are:
     * 
     * Email
     * Calendar visibility
     * 
     * Any other field is ignored.
     * 
     * @param dbEntry 
     * @param updated
     * @throws BusinessException 
     */
    private void setUserData(User dbEntry, User updated) {
        dbEntry.setEmail(updated.getEmail());
        dbEntry.setCalendarVisible(updated.isCalendarVisible());
    }

    public void remove(User u) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    private boolean checkPassword(int userID, String password) throws NotFoundException {
        return userDAO.retrieve(userID).getPassword().equals(User.encryptPassword(password));
    }

    public UserUserVisibility getVisibilityOverUser(int userID) throws NotFoundException {
        User user = userDAO.retrieve(userID);
        
        if(user.isCalendarVisible())
            return UserUserVisibility.VISIBLE;
        else
            return UserUserVisibility.NOT_VISIBLE;
    }

    @Override
    public boolean isUsernameInUse(String username) {
        return findByUsername(username) != null;
    }

    @Override
    public List<User> search(String username) {
        List<User> users = new ArrayList<User>();
        
        // TODO implement properly
        User found = userDAO.findByUsername(username);
        if(found != null)
            users.add(found);
        
        return users;
    }


}
