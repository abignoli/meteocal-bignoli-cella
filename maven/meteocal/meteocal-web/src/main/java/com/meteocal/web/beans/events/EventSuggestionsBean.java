/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.events;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.WeatherForecastBase;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.web.utility.SessionUtility;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

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

    private List<WeatherForecastBase> listOfSuggestions;
    private LocalDateTime start, end;
    private String name;
    private HttpServletResponse response;

    private int eventID;

    @PostConstruct
    public void init() {
        getID();
        setValues();
    }

    public EventSuggestionsBean() {
    }

    public String eventEditing() {
        return "/protected/event/EventPageCreator.xhtml?faces-redirect=true";
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

    private void getID() {
        eventID = sessionUtility.getParameter();
    }

    public void setValues() {
        setName(ef.find(eventID).getName());
        try {
            listOfSuggestions = ef.askSuggestedChange(eventID);
        }
        catch (NotFoundException ex) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "Error");
        }

        if (listOfSuggestions != null && !listOfSuggestions.isEmpty()) {
            start = listOfSuggestions.get(0).getForecastStart();
            end = listOfSuggestions.get(listOfSuggestions.size() - 1).getForecastEnd();
        }
        else {
            Event tmpEvent = ef.find(eventID);
            start = tmpEvent.getStart();
            end = tmpEvent.getEnd();
        }
    }
}
