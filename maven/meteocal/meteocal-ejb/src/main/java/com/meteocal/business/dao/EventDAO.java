/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.dao;

import com.meteocal.business.entities.Event;

/**
 *
 * @author Andrea Bignoli
 */
public class EventDAO extends DAObase<Event> {
    
    public EventDAO() {
	super(Event.class);
    }
}