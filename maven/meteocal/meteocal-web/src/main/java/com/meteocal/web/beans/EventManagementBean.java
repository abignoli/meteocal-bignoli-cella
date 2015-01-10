/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import com.meteocal.business.security.UserManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@RequestScoped
@Named
public class EventManagementBean implements Serializable {
    private User u;
    
    @EJB
    UserManager um;

    @PostConstruct
    public void init(){
        u = um.getLoggedUser();
    }
    
    public List<Event> getEvents(){
        
        /*
            List<Event> participatingTo = u.getParticipatingTo();
        
        System.out.println("events list: " + participatingTo.size());
        return participatingTo;
                
        */
        List<Event> participatingTo = new ArrayList<>();
        participatingTo.add(new Event());
        participatingTo.add(new Event());
        participatingTo.add(new Event());
        return participatingTo;
    }
                
    
}
