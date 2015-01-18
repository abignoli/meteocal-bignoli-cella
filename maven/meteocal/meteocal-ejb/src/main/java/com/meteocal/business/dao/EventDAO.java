/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.dao;

import com.meteocal.business.entities.Event;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;

/**
 *
 * @author Andrea Bignoli
 */
@Stateless
public class EventDAO extends DAObase<Event> {
    
    public EventDAO() {
	super(Event.class);
    }
    
    public Event findByName(String name) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", name);
        String query = "select e from Event e where e.name = :name";
        return super.findSingleResult(query, parameters);
    }
}
