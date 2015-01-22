/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.events;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import com.meteocal.business.entities.shared.WeatherCondition;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.business.facade.UserFacade;
import com.meteocal.web.exceptions.NotValidParameter;
import com.meteocal.web.utility.SessionUtility;
import java.io.Serializable;
import java.util.EnumSet;
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
public class EventCreatorBean implements Serializable {

    private String user, name;
    private Event referredEvent;
    private final HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    private int eventID;

    @Inject
    private SessionUtility sessionUtility;
    
    @EJB
    UserFacade uf;
    
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
        try {
            id = this.getID();
        }
        catch (NotValidParameter ex) {

        }
        setReferredEvent(ef.find(id));
    }

    public void addParticipant() {
        int userID;
        userID = uf.findByUsername(user).getId();  
        try {
            ef.addParticipant(eventID, userID);
        }
        catch (BusinessException ex) {
            Logger.getLogger(EventCreatorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getID() throws NotValidParameter {
        String strID;

        if (!sessionUtility.isAParameter()) {
            strID = request.getParameter("eventID");
            try {
                id = Integer.parseInt(strID);
            }
            catch (NumberFormatException e) {
                throw new NotValidParameter(NotValidParameter.MISSING_PARAMETER);
            }
        }
        else{
            id = sessionUtility.getParameterAsClient();
        }
        
        return id;
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

    public void setName() {
        name = referredEvent.getName();
    }

    public String getAddress() {
        return referredEvent.getAddress();
    }

    public void setAddress() {
        address = referredEvent.getAddress();
    }

    public String getCity() {
        return referredEvent.getCity();
    }

    public void setCity() {
        city = referredEvent.getCity();
    }

    public String getCountry() {
        return referredEvent.getCountry();
    }

    public void setCountry() {
        country = referredEvent.getCountry();
    }

    public String getDescription() {
        return referredEvent.getDescription();
    }

    public void setDescription() {
        description = referredEvent.getDescription();
    }

    public EnumSet<WeatherCondition> getAdverseConditions() {
        return referredEvent.getAdverseConditions();
    }

    public void setAdverseConditions() {
        adverseConditions = referredEvent.getAdverseConditions();
    }

    public List<User> getInvited() {
        return referredEvent.getInvited();
    }

    public void setInvited() {
        invited = referredEvent.getInvited();
    }

    public List<User> getParticipant() {
        return referredEvent.getParticipants();
    }

    public void setParticipant() {
        participants.add(referredEvent.findParticipant(null));
    }

    public void setIndoor() {
        indoor = referredEvent.isIndoor();
    }

    public boolean getIndoor() {
        return referredEvent.isIndoor();
    } 
}
