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
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.shared.security.UserEventVisibility;
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
     * Updates just schedule data.
     * 
     * Any other field is ignored.
     * 
     * @param eventID
     * The ID of the entity to update
     * @param start
     * @param end
     * @throws BusinessException 
     */
    public void updateScheduling(int eventID, int start, int end) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
     * State
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
        setEventData(dbEntry, e);
    }

    /**
     * Sets event data for the managed entity dbEntry using data provided by updated.
     * The updated fields are:
     * 
     * Name
     * Description
     * Country
     * City
     * Address
     * Indoor flag
     * Privacy flag
     * State
     * Start
     * End
     * 
     * Any other field is ignored.
     * 
     * @param dbEntry 
     * @param updated
     * @throws BusinessException 
     */
    private void setEventData(Event dbEntry, Event updated) {
        dbEntry.setName(updated.getName());
        dbEntry.setDescription(updated.getDescription());
        dbEntry.setCountry(updated.getCountry());
        dbEntry.setCity(updated.getCity());
        dbEntry.setAddress(updated.getAddress());
        dbEntry.setIndoor(updated.isIndoor());
        dbEntry.setPrivateEvent(updated.isPrivateEvent());

        // TODO Add event state, datetime
    }

    /**
     * {@inheritDoc}
     */
    public UserEventVisibility getVisibilityOnEvent(int userID, int eventID) throws NotFoundException {
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




}
