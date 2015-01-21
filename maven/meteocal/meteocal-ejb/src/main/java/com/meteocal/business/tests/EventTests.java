/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.tests;

import com.meteocal.business.dao.EventDAO;
import com.meteocal.business.entities.Event;
import java.time.LocalDateTime;
import java.util.List;
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
}
