/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.events;

import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.web.utility.SessionUtility;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author Leo
 */
@RequestScoped
@Named
public class EventSuggestionsBean implements Serializable {

    @EJB
    private EventFacade ef;

    @Inject
    private SessionUtility sessionUtility;

    private LocalDateTime start, end;
    private String name;
    
    private int eventID;

    @PostConstruct
    public void init(){
    }
    
    public EventSuggestionsBean() {
    }

    public String eventEditing() {
        return "/protected/event/EventPageCreator.xhtml?";
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void accept() throws BusinessException {
        ef.updateScheduling(eventID, start, end);
        ef.find(eventID).clearSuggestedChange();
    }

    public void discard() {
        ef.find(eventID).clearSuggestedChange();
    }

    private int getID() {
        eventID = sessionUtility.getParameter();
        return eventID;
    }
}
