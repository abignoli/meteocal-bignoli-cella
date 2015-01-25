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
import com.meteocal.business.entities.WeatherForecastBase;
import com.meteocal.business.entities.shared.EventStatus;
import com.meteocal.business.entities.shared.WeatherCondition;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.InvalidInputException;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.security.UserManager;
import com.meteocal.business.shared.security.UserEventVisibility;
import com.meteocal.business.shared.utils.RegExUtils;
import com.meteocal.shared.ServiceVariables;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author USUARIO
 */
@Stateless
public class EventFacadeImplementation implements EventFacade {

    public static final String REXEX_PATTERN_INVITED_USER = "(\\s*(\\w*)\\s*,?)";
    public static final int REXEX_PATTERN_INVITED_USER_GROUP = 2;

    public static final String REXEX_PATTERN_INVITED_USERS = "(\\s*(\\w*)\\s*,?)*";

    @EJB
    private EventDAO eventDAO;

    @EJB
    private UserDAO userDAO;

    @EJB
    private UserFacade userFacade;

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
            eventDAO.flush();

            updateWeatherForecastsAsync(savedEvent);
        } catch (NotFoundException notFoundException) {
            throw new BusinessException(BusinessException.EVENT_CREATION_INTERNAL_PROCESSING);
        }

        return savedEvent;
    }

    public Event create(Event e, int userID) throws InvalidInputException, NotFoundException {
        User creator = userDAO.retrieve(userID);

        Event toSave = new Event(e);

        toSave.setCreator(creator);
        creator.getCreatedEvents().add(toSave);

        if (!toSave.validForSave()) {
            throw new InvalidInputException(InvalidInputException.EVENT_CREATION_INVALID);
        }

        eventDAO.save(toSave);
        return toSave;
    }

    public Event find(int eventID) {
        return eventDAO.findAndRefresh(eventID);
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

        if (e.setScheduling(start, end)) {
            schedulingChanged = true;
            
            eventDAO.update(e);
            eventDAO.flush();

            updateWeatherForecastsAsync(e);
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
        
        eventDAO.update(dbEntry);
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
    public void addInvited(int eventID, String username) throws BusinessException {
        Event e = eventDAO.retrieve(eventID);

        addInvited(e, username);
    }

    private void addInvited(Event e, String username) throws BusinessException {
        User u = userFacade.findByUsername(username);

        if (u != null) {
            e.addInvited(u);
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

    @Override
    public void addInvitedList(int eventID, String listInvited) throws BusinessException {
        List<String> usernames = RegExUtils.decomposeMultiple(listInvited, REXEX_PATTERN_INVITED_USER, REXEX_PATTERN_INVITED_USER_GROUP);

        Event e = eventDAO.retrieve(eventID);

        for (String username : usernames) {
            addInvited(e, username);
        }
    }

    public void updateWeatherForecasts(int eventID) throws InvalidInputException, NotFoundException {
        Event e = eventDAO.retrieve(eventID);

        updateWeatherForecasts(e);
    }

    private void updateWeatherForecasts(Event e) throws InvalidInputException, NotFoundException {
        if (e.getStatus() != EventStatus.PLANNED) {
            return;
        }

        // Ask new weather forecasts
        List<WeatherForecast> newForecasts = weatherForecastFacade.askWeatherForecasts(e.getId());
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
        weatherForecastFacade.save(newForecasts);

        if (!newForecastsGood && needsSuggestedScheduling(e)) {
            // TODO check suggested scheduling
            updateSuggestedScheduling(e);
        }
    }

    private void disableWeatherForecasts(Event e) {
        for (WeatherForecast wf : e.getWeatherForecasts()) {
            weatherForecastFacade.disable(wf);
        }
    }

    private void updateSuggestedScheduling(Event e) throws NotFoundException, InvalidInputException {
        List<WeatherForecastBase> suggestion = weatherForecastFacade.askSuggestion(e.getId());

        if (suggestion != null) {
            e.setSuggestedChangeAvailable(true);

            if (!e.isSuggestedChangeProvided()) {
                notificationFacade.createNotificationForSuggestedChange(e.getId());
                e.setSuggestedChangeProvided(true);
            }
        } else {
            e.setSuggestedChangeAvailable(false);
        }
    }

    @Override
    public boolean exists(int eventID) {
        return find(eventID) == null;
    }

    public List<Event> mask(List<Event> events) {
        List<Event> result = new ArrayList<Event>();
        
        // Make sure the events are detached
        for (Event e : events) {
            eventDAO.detach(e);
            
            result.add(mask(e));
        }

        return result;
    }

    private Event mask(Event e) {
        if(e.isPrivateEvent()) {
            Event result = new Event();
            
            result.setName("");

            result.setStart(e.getStart());
            result.setEnd(e.getEnd());

            return result;
        }
        
        return e;
    }

    @Override
    public List<Event> search(String eventName) {
        List<Event> events = new ArrayList<Event>();

        // TODO implement properly
        Event found = eventDAO.findByName(eventName);
        if (found != null) {
            events.add(found);
        }

        return events;
    }

    @Override
    public void updateWeatherForecasts() throws InvalidInputException, NotFoundException {
        for (Event e : getPlannedEvents()) {
            updateWeatherForecasts(e);
        }
    }

    private List<Event> getPlannedEvents() {
        return eventDAO.findPlanned();
    }

    @Asynchronous
    private void updateWeatherForecastsAsync(Event e) throws InvalidInputException, NotFoundException {
        updateWeatherForecasts(e);
    }

    @Override
    public boolean isSuggestedChangeAvailable(int eventID) throws NotFoundException {
        Event e = eventDAO.retrieve(eventID);

        return isInSuggestedChangeRange(e) && e.isSuggestedChangeAvailable();
    }

    @Override
    public void checkEventsSchedule() {
        List<Event> events = eventDAO.findPlanned();

        for (Event e : events) {
            if (e.getStart().isBefore(LocalDateTime.now())) {
                e.setStatus(EventStatus.CONCLUDED);
            }
        }
    }

    private boolean isInSuggestedChangeRange(Event e) {
        LocalDateTime max = LocalDateTime.now().minusHours(LocalDateTime.now().getHour()).plusDays(ServiceVariables.SUGGESTED_CHANGE_RANGE_DAYS_BEFORE_START + 1);

        return e.getStart().isBefore(max);
    }

    private boolean needsSuggestedScheduling(Event e) {
        return isInSuggestedChangeRange(e);
    }

    @Override
    public List<WeatherForecastBase> askSuggestedChange(int eventID) throws NotFoundException {
        Event e = eventDAO.retrieve(eventID);
        
        if(!needsSuggestedScheduling(e)) {
            return null;
        }
        
        try {
            return weatherForecastFacade.askSuggestion(eventID);
        } catch (InvalidInputException ex) {
            Logger.getLogger(EventFacadeImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

}
