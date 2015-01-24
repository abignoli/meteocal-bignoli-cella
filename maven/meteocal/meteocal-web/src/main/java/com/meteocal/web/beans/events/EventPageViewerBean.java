/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.events;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import com.meteocal.business.entities.WeatherForecast;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.business.security.UserManager;
import java.io.Serializable;
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
        setReferredEvent(ef.find(this.getId()));
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

    public List<User> getInvited(){
        return referredEvent.getInvited();
    }

    public List<User> getParticipant(){
        return referredEvent.getParticipants();
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
}
