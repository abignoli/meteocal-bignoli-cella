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
import com.meteocal.business.entities.WeatherForecast;
import com.meteocal.business.entities.shared.EventStatus;
import com.meteocal.business.entities.shared.WeatherCondition;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.InvalidInputException;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.security.UserManager;
import com.meteocal.business.shared.security.UserEventVisibility;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
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
    private WeatherForecastFacade weatherForecastFacade;

    @EJB
    private NotificationFacade notificationFacade;

    @EJB
    private UserManager userManager;

    public Event create(Event e) throws BusinessException {
        User creator = userManager.getLoggedUser();
        Event savedEvent;

        if (creator == null) {
            throw new InvalidInputException(InvalidInputException.EVENT_CREATION_NO_LOGGED_USER);
        }

        try {
            savedEvent = create(e, creator.getId());
        } catch (NotFoundException notFoundException) {
            throw new BusinessException(BusinessException.EVENT_CREATION_INTERNAL_PROCESSING);
        }
        
        return savedEvent;
    }


    public Event create(Event e, int userID) throws InvalidInputException, NotFoundException {
        User creator = userDAO.retrieve(userID);

        Event toSave = new Event(e);

        toSave.setCreator(creator);

        if (!toSave.validForSave()) {
            throw new InvalidInputException(InvalidInputException.EVENT_CREATION_INVALID);
        }

        eventDAO.save(toSave);
        return toSave;
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

        if (e.isParticipant(u)) {
            e.removeParticipant(u);
        } else {
            e.addParticipant(u);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean updateScheduling(int eventID, LocalDateTime start, LocalDateTime end) throws BusinessException {
        Event e = eventDAO.retrieve(eventID);

        return updateScheduling(e, start, end);
    }

    private boolean updateScheduling(Event e, LocalDateTime start, LocalDateTime end) throws InvalidInputException, NotFoundException {
        boolean schedulingChanged = false;
        Event.validateScheduling(start, end);

        if (e.hasDifferentScheduling(start, end)) {
            e.setStart(start);
            e.setEnd(end);

            schedulingChanged = true;

            e.clearSuggestedChange();

            notificationFacade.createNotificationForEventChange(e.getId());

            updateWeatherForecasts(e);
        }

        return schedulingChanged;
    }

    /**
     * {@inheritDoc}
     */
    public void updateData(Event e) throws BusinessException {
        Event dbEntry = eventDAO.retrieve(e.getId());

        // Sets event data
        dbEntry.setEventData(e);
        notificationFacade.createNotificationForEventChange(e.getId());

        // Updates scheduling and checks weather forecasts
        updateScheduling(dbEntry, e.getStart(), e.getEnd());
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

        if (event.getCreator().getId() == user.getId()) {
            return UserEventVisibility.CREATOR;
        }

        if (event.isInvited(user)) {
            return UserEventVisibility.VIEWER;
        }

        if (event.isPrivateEvent()) {
            return UserEventVisibility.NO_VISIBILITY;
        } else {
            return UserEventVisibility.VIEWER;
        }
    }

    @Override
    public void addInvited(int eventID, int userID) throws BusinessException {
        Event e = eventDAO.retrieve(eventID);
        User u = userDAO.retrieve(userID);

        if (!e.addInvited(u)) {
            throw new BusinessException(InvalidInputException.USER_ALREADY_INVITED);
        }
    }

    public void updateWeatherForecasts(int eventID) throws InvalidInputException, NotFoundException {
        Event e = eventDAO.retrieve(eventID);

        updateWeatherForecasts(e);
    }

    private void updateWeatherForecasts(Event e) throws InvalidInputException, NotFoundException {
        // Ask new weather forecasts
        List<WeatherForecast> newForecasts = weatherForecastFacade.askWeatherForecasts(e.getStart(), e.getEnd());
        boolean newForecastsGood = e.areForecastsGood(newForecasts);

        if (e.getWeatherForecasts() != null && e.getWeatherForecasts().size() != 0) {
            // Check good/ bad
            boolean currentForecastsGood = e.areCurrentForecastsGood();

            // Disable current weather forecasts
            disableWeatherForecasts(e);

            // Produce notification if good -> bad, bad -> good
            if (newForecastsGood != currentForecastsGood) {
                // Produce notification
                notificationFacade.createNotificationForWeatherConditions(e.getId(), newForecastsGood);
            }
        } else if (!newForecastsGood) {
            // Produce notification
            notificationFacade.createNotificationForWeatherConditions(e.getId(), newForecastsGood);
        }

        // Update event with new forecasts
        e.setWeatherForecasts(newForecasts);

        if (!newForecastsGood) {
            updateSuggestedScheduling(e);
        }
    }

    private void disableWeatherForecasts(Event e) {
        for (WeatherForecast wf : e.getWeatherForecasts()) {
            weatherForecastFacade.disable(wf);
        }
    }

    private void updateSuggestedScheduling(Event e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean exists(int eventID) {
        return find(eventID) == null;
    }

    public List<Event> mask(List<Event> events) {
        // Make sure the events are detached
        for(Event e: events) {
            eventDAO.detach(e);
            
            mask(e);
        }
        
        return events;
    }

    private void mask(Event e) {
        Event result = new Event();
        
        result.setStart(e.getStart());
        result.setEnd(e.getEnd());
        
        e = result;
    }
}
