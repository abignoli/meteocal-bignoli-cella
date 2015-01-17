/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.security;

import com.meteocal.business.entities.User;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.business.facade.UserFacade;
import com.meteocal.business.shared.data.Group;
import com.meteocal.business.shared.security.UserEventVisibility;
import java.security.Principal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Andrea Bignoli
 */
@Stateless
public class UserManager {
    
    @EJB
    UserFacade userFacade;
    
    @EJB
    EventFacade eventFacade;
    
    @Inject
    Principal principal;
    
    public void register(User user) {
        user.setGroupName(Group.USER.getName());
        userFacade.save(user);
    }
    
    public void unregister() {
        userFacade.remove(getLoggedUser());
    }

    public User getLoggedUser() {
        return userFacade.findByUsername(principal.getName());
    }
    
    /**
     * Gets the type of visibility that the logged user has over the event identified by eventID.
     * 
     * @param eventID
     * @return
     * @throws NotFoundException 
     * If the requested eventID doesn't exist
     */
    public UserEventVisibility getVisibilityOverEvent(int eventID) throws NotFoundException {
        return eventFacade.getVisibilityOverEvent(getLoggedUser().getId(), eventID);
    }
}
