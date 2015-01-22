/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.entities.Event;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.InvalidInputException;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.shared.security.UserEventVisibility;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public interface EventFacade {

    /**
     * Create a new event in the database based on data from the provided one.
     * The updated fields are:
     *
     * Name Description Country City Address Indoor flag Privacy flag Adverse
     * weather conditions set Start End
     *
     * The creator is the logged user, provided by the UserManager.
     *
     * The status is set to EventStatus.PLANNED.
     *
     * The creation include a validation phase.
     *
     * @param e
     * @return the refernce to the saved event
     * @throws InvalidInputException If the validation phase fails.
     */
    public abstract Event create(Event e) throws BusinessException;

    /**
     * Create a new event in the database based on data from the provided one.
     * The updated fields are:
     *
     * Name Description Country City Address Indoor flag Privacy flag Adverse
     * weather conditions set Start End
     *
     * The creator is the user identified by the provided userID.
     *
     * The status is set to EventStatus.PLANNED.
     *
     * The creation include a validation phase.
     *
     * @param e
     * @return the refernce to the saved event
     * @throws InvalidInputException If the validation phase fails.
     */
    public abstract Event create(Event e, int userID) throws InvalidInputException, NotFoundException;

    public abstract Event find(int eventID);
    
    public abstract boolean exists(int eventID);

    public abstract void addInvited(int eventID, int userID) throws BusinessException;

    public abstract void addParticipant(int eventID, int userID) throws BusinessException;

    public abstract void removeParticipant(int eventID, int userID) throws BusinessException;

    public abstract void toggleParticipant(int eventID, int userID) throws BusinessException;

    /**
     * Updates just schedule data about the start and the end of the event.
     *
     * Any other field is ignored.
     *
     * @param eventID The ID of the entity to update
     * @param start
     * @param end
     * @return true if the updated schedule is different from the previous one,
     * false otherwise
     * @throws BusinessException
     */
    public abstract boolean updateScheduling(int eventID, LocalDateTime start, LocalDateTime end) throws BusinessException;

    /**
     * Updates event data for the database entry identified by the ID of the
     * event e. The updated fields are:
     *
     * Name Description Country City Address Indoor flag Privacy flag Start End
     *
     * Any other field is ignored.
     *
     * @param e The entity containing the updated data, as well as the ID of the
     * entity to update
     * @throws BusinessException
     */
    public abstract void updateData(Event e) throws BusinessException;

    public void cancel(int eventID) throws BusinessException;

    /**
     * Gets the type of visibility that the user identified by userID has over
     * the event identified by eventID.
     *
     * @param userID
     * @param eventID
     * @return
     * @throws NotFoundException If the requested userID or eventID doesn't
     * exist
     */
    public UserEventVisibility getVisibilityOverEvent(int userID, int eventID) throws NotFoundException;

    public void updateWeatherForecasts(int eventID) throws InvalidInputException, NotFoundException;

    public List<Event> mask(List<Event> createdAndParticipatingTo);
    
    public abstract List<Event> search(String eventName);
}
