/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.tests;

import com.meteocal.business.dao.EventDAO;
import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.InvalidInputException;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.business.security.UserManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AB
 */
@Stateless
public class EventTests {
    
    @EJB
    EventDAO eventDAO;
    
    @EJB
    EventFacade eventFacade;
    
    @EJB
    UserManager userManager;
    
    private static final String EVENT_NAME_CREATE_WITH_DATE = "Test create with date";
    
    public void testCreateWithDate() {
        Event e = new Event();
        
        e.setName(EVENT_NAME_CREATE_WITH_DATE);
        e.setDescription("desc");
        e.setStart(LocalDateTime.now());
        
        eventDAO.save(e);
    }
    
    public void checkCreateWithDate() {
        Event e = eventDAO.findByName(EVENT_NAME_CREATE_WITH_DATE);
        
        System.out.println("[TEST - checkCreateWithDate] The event was created: " + e.getStart().toString());
    }
    
    public List<Event> findAll() {
        List<Event> result = eventDAO.findAll();
        return result;
    }
    
    public User testCreateAndForecast1() {
        User creator = new User();
        creator.setUsername("u1");
        creator.setPassword("pw");
        creator = userManager.register(creator);
        return creator;
    }
    
    public Event testCreateAndForecast2(int creatorID) {
        Event e = new Event();
        e.setName("n");
        e.setDescription("d");
        e.setAddress("");
        e.setIndoor(false);
        e.setPrivateEvent(true);
        
        e.setCity("Milano");
        e.setCountry("ITALY");
        e.setStart(LocalDateTime.now().plusDays(1));
        e.setEnd(LocalDateTime.now().plusDays(2).minusHours(2));
        
        try {
            return eventFacade.create(e, creatorID);
        } catch (InvalidInputException ex) {
            Logger.getLogger(EventTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            Logger.getLogger(EventTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public void testCreateAndForecast3(int eventID) {
        try {
            eventFacade.updateWeatherForecasts(eventID);
        } catch (InvalidInputException ex) {
            Logger.getLogger(EventTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            Logger.getLogger(EventTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public User testCreateAndAskSuggestion1() {
        User creator = new User();
        creator.setUsername("u1");
        creator.setPassword("pw");
        creator = userManager.register(creator);
        return creator;
    }
    
    public Event testCreateAndAskSuggestion2(int creatorID) {
        Event e = new Event();
        e.setName("n");
        e.setDescription("d");
        e.setAddress("");
        e.setIndoor(false);
        e.setPrivateEvent(true);
        
        e.setCity("Milano");
        e.setCountry("ITALY");
        e.setStart(LocalDateTime.now().plusDays(1));
        e.setEnd(LocalDateTime.now().plusDays(2).minusHours(2));
        
        try {
            return eventFacade.create(e, creatorID);
        } catch (InvalidInputException ex) {
            Logger.getLogger(EventTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            Logger.getLogger(EventTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public void testCreateAndAskSuggestion3(int eventID) {
        try {
            eventFacade.askSuggestedChange(eventID);
        } catch (NotFoundException ex) {
            Logger.getLogger(EventTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
