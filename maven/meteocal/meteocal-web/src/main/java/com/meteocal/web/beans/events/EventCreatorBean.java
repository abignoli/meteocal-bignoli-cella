/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.events;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import com.meteocal.business.entities.shared.WeatherCondition;
import com.meteocal.business.facade.EventFacade;
import java.io.Serializable;
import java.util.EnumSet;
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
public class EventCreatorBean implements Serializable{

    private String user,name;
    private Event referredEvent;
    private final HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    private int eventID;
    
    @EJB
    EventFacade ef;
    private String address;
    private String city;
    private String country;
    private String description;
    private EnumSet<WeatherCondition> adverseConditions;
    private List<User> invited;
    private List<User> participants;
    private int id;
    private boolean indoor;

    @PostConstruct
    public void init() {
        setReferredEvent(ef.find(this.getId()));
    }

    public String addParticipant() {
        return "/protected/event/EventPageCreator.xhtml";
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
    public void setName(){
        name = referredEvent.getName();
    }
    
    public String getAddress(){
        return referredEvent.getAddress();
    }
    public void setAddress(){
        address = referredEvent.getAddress();
    }
    
    public String getCity(){
        return referredEvent.getCity();
    }
    public void setCity(){
        city = referredEvent.getCity();
    }
    
    public String getCountry(){
        return referredEvent.getCountry();
    }
    public void setCountry(){
        country = referredEvent.getCountry();
    }
    
    public String getDescription(){
        return referredEvent.getDescription();
    }
    public void setDescription(){
        description = referredEvent.getDescription();
    }
    
    public EnumSet<WeatherCondition> getAdverseConditions(){
        return referredEvent.getAdverseConditions();
    }
    public void setAdverseConditions(){
        adverseConditions = referredEvent.getAdverseConditions();
    }
    
    public List<User> getInvited(){
        return referredEvent.getInvited();
    }
    public void setInvited(){
        invited = referredEvent.getInvited();
    }
    
    public List<User> getParticipant(){
        return referredEvent.getParticipants();
    }
    public void setParticipant(){
        participants.add(referredEvent.findParticipant(null));
    }
    
    public void setIndoor(){
        indoor = referredEvent.isIndoor();
    }
    
    public boolean getIndoor(){
        return referredEvent.isIndoor();
    }
}
