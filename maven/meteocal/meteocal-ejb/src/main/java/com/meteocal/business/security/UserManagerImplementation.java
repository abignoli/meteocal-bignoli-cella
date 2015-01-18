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

/**
 *
 * @author ate
 */
@Stateless
public class UserManagerImplementation implements UserManager {

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
     * {@inheritDoc}
     */
    public UserEventVisibility getVisibilityOverEvent(int eventID) throws NotFoundException {
        return eventFacade.getVisibilityOverEvent(getLoggedUser().getId(), eventID);
    }

    /**
     * {@inheritDoc}
     */
    public UserUserVisibility getVisibilityOverUser(int userID) throws NotFoundException {
        return userFacade.getVisibilityOverUser(userID);
    }

    public void addParticipation(int eventID) throws BusinessException {
        eventFacade.addParticipant(eventID, getLoggedUser().getId());
    }

    public void removeParticipation(int eventID) throws BusinessException {
        eventFacade.removeParticipant(eventID, getLoggedUser().getId());
    }

    public void toggleParticipation(int eventID) throws BusinessException {
        eventFacade.toggleParticipant(eventID, getLoggedUser().getId());
    }
}
