/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.security;

import com.meteocal.business.entities.User;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.business.facade.UserFacade;
import com.meteocal.business.shared.data.Group;
import com.meteocal.business.shared.security.UserEventVisibility;
import com.meteocal.business.shared.security.UserUserVisibility;
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
public interface UserManager {

    public void register(User user);
    
    public void unregister();

    public User getLoggedUser();
    
    /**
     * Gets the type of visibility that the logged user has over the event identified by eventID.
     * 
     * @param eventID
     * @return
     * @throws NotFoundException 
     * If the requested eventID doesn't exist
     */
    public UserEventVisibility getVisibilityOverEvent(int eventID) throws NotFoundException;
    
    /**
     * Gets the type of visibility that the logged user has over the user identified by userID.
     * 
     * @param userID
     * @return
     * @throws NotFoundException 
     * If the requested userID doesn't exist
     */
    public UserUserVisibility getVisibilityOverUser(int userID) throws NotFoundException;
    
    public void addParticipation(int eventID) throws BusinessException;
    
    public void removeParticipation(int eventID) throws BusinessException;
    
    public void toggleParticipation(int eventID) throws BusinessException;
}
