/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans;

import com.meteocal.business.entities.Event;
import com.meteocal.business.facade.EventFacade;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@Named
@RequestScoped
public class EventCreationBean {
    
    @EJB
    private EventFacade ef;
    
    private Event createdEvent;

    private boolean indoor, privateEvent;
    
    
    @PostConstruct
    public void init(){
        this.setEvent(new Event());
    }

    public String eventCreation(){
        return "/protected/event/EventPageCreator";
    }
    
    private void setEvent(Event event) {
        this.createdEvent = event;
    }
    
    public String create(){
        ef.create(getCreatedEvent());
        return "/EventPage";
    }
    
    public Event getCreatedEvent(){
        return this.createdEvent;
    }
    
    public void setCreatedEvent(Event event){
        this.createdEvent = event;
    }
}
