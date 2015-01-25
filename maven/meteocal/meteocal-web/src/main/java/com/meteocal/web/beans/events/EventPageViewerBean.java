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
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
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
    
    @EJB
    UserManager um;
    
    private int id;

    @PostConstruct
    public void init() {
        id = getId();
        setReferredEvent(ef.find(id));
    }

    public int getId() {
        String strID;
        strID = request.getParameter("eventID");
        id = Integer.parseInt(strID);
        return id;
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
