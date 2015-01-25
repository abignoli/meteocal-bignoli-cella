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
 * @author Leo
 */
@ManagedBean
@RequestScoped
public class EventPageViewerBean implements Serializable{

    private String user,name;
    private Event referredEvent;
    private final HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    private int eventID;
    
    @EJB
    EventFacade ef;
    
    @Inject
    SessionUtility sessionUtility;
    
    @EJB
    UserManager um;
    
    private int id;

    @PostConstruct
    public void init() {
        try {
            id = getId();
            setReferredEvent(ef.find(id));
        }
        catch (NotValidParameter ex) {
            Logger.getLogger(EventPageViewerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getId() throws NotValidParameter {
        String strID;

        if (!sessionUtility.isAParameter()) {
            strID = request.getParameter("eventID");
            try {
                eventID = Integer.parseInt(strID);
            }
            catch (NumberFormatException e) {
                throw new NotValidParameter(NotValidParameter.MISSING_PARAMETER);
            }
        }
        else {
            eventID = sessionUtility.getParameter();
        }

        return eventID;
    }
    
    public void setReferredEvent(Event event){
        referredEvent = event;
    }
    
    public Event getReferredEvent(){
        return referredEvent;
    }
    
    public String getName(){
        return referredEvent.getName();
    }
    
    public String getAddress(){
        return referredEvent.getAddress();
    }

    public String getCity(){
        return referredEvent.getCity();
    }
    
    public String getCountry(){
        return referredEvent.getCountry();
    }

    public String getDescription(){
        return referredEvent.getDescription();
    }

    public List<WeatherForecast> getAdverseConditions(){
        return referredEvent.getWeatherForecasts();
    }

    public String getInvited() {
        return referredEvent.getInvitedAsString();
    }

    public String getParticipants() {
        return referredEvent.getParticipantsAsString();
    }
    
    public boolean getIndoor(){
        return referredEvent.isIndoor();
    }
    
    public boolean getPrivate() {
        return referredEvent.isPrivateEvent();
    }
    
    public void cancelPartecipation() throws BusinessException{
        ef.removeParticipant(id, um.getLoggedUser().getId());
    }
    
    public String getStart() {
        return referredEvent.getStart().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }
    
    public String getEnd() {
        return referredEvent.getEnd().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }
    
    public String getWeatherConditionsToAvoid() {
        String result = "";
        
        for(WeatherCondition wc: referredEvent.getAdverseConditions()) {
            result += wc.toString() + " ";
        }
        
        return result;
    }
    
    public List<WeatherForecast> getWeatherForecasts() {
        return referredEvent.getWeatherForecasts();
    }
}
