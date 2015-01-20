/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.business.facade.UserFacade;
import com.meteocal.business.security.UserManager;
import com.meteocal.web.utility.SessionUtility;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@RequestScoped
@Named
public class EventManagementBean implements Serializable {
    private List<Event> events;
    private String username;
    
    @Inject
    SessionUtility sessionUtility;
    
    @EJB
    UserManager uf;

    @PostConstruct
    public void init(){
        username = sessionUtility.getLoggedUser();
    }
    
    public List<Event> getEvents(){
        return events;
    }
    
    public void setEvents(List<Event> newEvents){
        this.events = newEvents;
    }
    
    
                
    
}
