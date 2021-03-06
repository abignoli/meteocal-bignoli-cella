/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.dao;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.shared.EventStatus;
import com.meteocal.business.entities.shared.TableDictionary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;

/**
 *
 * @author Andrea Bignoli
 */
@Stateless
public class EventDAO extends DAObase<Event> {
    
    public EventDAO() {
	super(Event.class, TableDictionary.TABLE_EVENT);
    }
    
    public Event findByName(String name) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", name);
        String query = "select e from Event e where e.name = :name";
        return super.findSingleResult(query, parameters);
    }
    
    public List<Event> findPlanned() {
//        String query = "select e from Event e where e.status = PLANNED";
//        return super.findResults(query);
        List<Event> events = findAll();
        List<Event> planned = new ArrayList<Event>();
        
        if(events != null) {
            for(Event e: events)
                if(e.getStatus() == EventStatus.PLANNED) {
                    planned.add(e);
                    refresh(e);
                }
        }
        
        return planned;
    }
    
    public List<Event> findToUpdateForWeatherForecast() {
        // TODO add proper checks
        return findPlanned();
    }
}
