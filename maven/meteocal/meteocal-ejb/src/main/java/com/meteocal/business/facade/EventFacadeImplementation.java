/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.dao.EventDAO;
import com.meteocal.business.dao.UserDAO;
import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import com.meteocal.business.entities.shared.EventStatus;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.InvalidInputException;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.shared.security.UserEventVisibility;
import java.time.LocalDateTime;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author USUARIO
 */
@Stateless
public class EventFacadeImplementation implements EventFacade {

    @EJB
    private EventDAO eventDAO;
    
    @EJB
    private UserDAO userDAO;
    
    @EJB
    private NotificationFacade notificationFacade;
    
    public Event create(Event e) {
        eventDAO.save(e);
        return e;
    }
        
    public Event find(int eventID) {
        return eventDAO.find(eventID);
    }
    
    
    public void addParticipant(int eventID, int userID) throws BusinessException {
        Event e = eventDAO.retrieve(eventID);
        User u = userDAO.retrieve(userID);
        
        e.addParticipant(u);
    }
    
    public void removeParticipant(int eventID, int userID) throws BusinessException {
        Event e = eventDAO.retrieve(eventID);
        User u = userDAO.retrieve(userID);
        
        e.removeParticipant(u);
    }
    
    public void toggleParticipant(int eventID, int userID) throws BusinessException {
        Event e = eventDAO.retrieve(eventID);
        User u = userDAO.retrieve(userID);
        
        if(e.isParticipant(u))
            e.removeParticipant(u);
        else
            e.addParticipant(u);
    }
    
    
    /**
     * Updates just schedule data about the start and the end of the event.
     * 
     * Any other field is ignored.
     * 
     * @param eventID
     * The ID of the entity to update
     * @param start
     * @param end
     * @throws BusinessException 
     */
    public void updateScheduling(int eventID, LocalDateTime start, LocalDateTime end) throws BusinessException {
    
        Event.validateScheduling(start, end);
        
        Event e = eventDAO.retrieve(eventID);
        
        e.setStart(start);
        e.setEnd(end);
        
        e.setSuggestedChangeStart(null);
        e.setSuggestedChangeEnd(null);
        
        notificationFacade.createNotificationForEventChange(eventID);
        
        // TODO Clean weather forecasts
        
        // TODO Check event weather conditions
    }

    /**
     * Updates event data for the database entry identified by the ID of the event e.
     * The updated fields are:
     * 
     * Name
     * Description
     * Country
     * City
     * Address
     * Indoor flag
     * Privacy flag
     * Start
     * End
     * 
     * Any other field is ignored.
     * 
     * @param e
     * The entity containing the updated data, as well as the ID of the entity to update
     * @throws BusinessException 
     */
    public void updateData(Event e) throws BusinessException {
        Event dbEntry = eventDAO.retrieve(e.getId());
        
        dbEntry.setEventData(e);
        
        notificationFacade.createNotificationForEventChange(e.getId());
        
        // TODO Clean weather forecasts
        
        // TODO Check event weather conditions
    }
    
    public void cancel(int eventID) throws BusinessException {
        Event e = eventDAO.retrieve(eventID);
        
        e.cancel();
        
        notificationFacade.createNotificationForEventCancel(eventID);
    }

    /**
     * {@inheritDoc}
     */
    public UserEventVisibility getVisibilityOverEvent(int userID, int eventID) throws NotFoundException {
        Event event = eventDAO.retrieve(eventID);
        User user = userDAO.retrieve(userID);
        
        if(event.getCreator().getId() == user.getId())
            return UserEventVisibility.CREATOR;
        
        if(event.isInvited(user))
            return UserEventVisibility.VIEWER;
        
        if(event.isPrivateEvent())
            return UserEventVisibility.NO_VISIBILITY;
        else
            return UserEventVisibility.VIEWER;
    }

    @Override
    public void addInvited(int eventID, int userID) throws BusinessException {
        Event e = eventDAO.retrieve(eventID);
        User u = userDAO.retrieve(userID);
        
        if(!e.addInvited(u))
            throw new BusinessException(InvalidInputException.USER_ALREADY_INVITED);
    }






}
