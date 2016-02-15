/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.events;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import com.meteocal.business.entities.WeatherForecast;
import com.meteocal.business.entities.shared.WeatherCondition;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.business.security.UserManager;
import com.meteocal.web.exceptions.NotValidParameter;
import com.meteocal.web.utility.SessionUtility;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Leonardo Cella
 */
@ManagedBean
@RequestScoped
public class EventPageViewerBean implements Serializable {

    private Event referredEvent;
    private final HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    private final String errorOutcome = "Error";

    @EJB
    EventFacade ef;

    @Inject
    SessionUtility sessionUtility;

    @EJB
    UserManager um;

    private int id;

    @PostConstruct
    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();

        try {
            id = getId();
        }
        catch (NotValidParameter ex) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, errorOutcome);
        }
        setReferredEvent(ef.find(id));
    }

    public int getId() throws NotValidParameter {
        String strID;
        int eventID;
        if (!sessionUtility.isAParameter()) {
            strID = request.getParameter("eventID");
            try {
                eventID = Integer.parseInt(strID);
            }
            catch (NumberFormatException e) {
                eventID = sessionUtility.getParameterAsClient();
            }
        }
        else {
            eventID = sessionUtility.getParameterAsClient();
        }

        return eventID;
    }

    public void setReferredEvent(Event event) {
        referredEvent = event;
    }

    public Event getReferredEvent() {
        return referredEvent;
    }

    public String getName() {
        return referredEvent.getName();
    }

    public String getAddress() {
        return referredEvent.getAddress();
    }

    public String getCity() {
        return referredEvent.getCity();
    }

    public String getCountry() {
        return referredEvent.getCountry();
    }

    public String getDescription() {
        return referredEvent.getDescription();
    }

    public List<WeatherForecast> getAdverseConditions() {
        return referredEvent.getWeatherForecasts();
    }

    public String getInvited() {
        return referredEvent.getInvitedAsString();
    }

    public String getParticipants() {
        return referredEvent.getParticipantsAsString();
    }

    public boolean getIndoor() {
        return referredEvent.isIndoor();
    }

    public boolean getPrivate() {
        return referredEvent.isPrivateEvent();
    }

    public String cancelPartecipation() {
        try {
            um.removeParticipation(id);
        }
        catch (BusinessException ex) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "Error");
        }
        sessionUtility.setParameter(id);
        return "/protected/event/EventPageViewer?faces-redirect=true";
    }

    public String confirmPartecipation() {
        try {
            um.addParticipation(id);
        }
        catch (BusinessException ex) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "Error");
        }
        sessionUtility.setParameter(id);
        return "/protected/event/EventPageViewer?faces-redirect=true";
    }

    public boolean getRenderDelete() {
        return ef.find(id).isParticipant(um.getLoggedUser());
    }

    public String getStart() {
        return referredEvent.getStart().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    public String getEnd() {
        return referredEvent.getEnd().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    public String getWeatherConditionsToAvoid() {
        String result = "";

        for (WeatherCondition wc : referredEvent.getAdverseConditions()) {
            result += wc.toString() + " ";
        }

        return result;
    }

    public boolean getParticipantFlag() {
        try {
            return um.isLoggedUserParticipatingTo(id);
        }
        catch (NotFoundException nfe) {

        }

        return false;
    }

    public List<WeatherForecast> getWeatherForecasts() {
        return referredEvent.getWeatherForecasts();
    }
}
