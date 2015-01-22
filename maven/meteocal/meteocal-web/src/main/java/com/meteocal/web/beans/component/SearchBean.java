/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.component;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.business.facade.UserFacade;
import com.meteocal.business.security.UserManager;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author Leo
 */
@RequestScoped
@ManagedBean
public class SearchBean implements Serializable {

    private String searched;
    private final String resultsOutcome = "Results";
    private List<User> users;
    private List<Event> events;

    public String getSearched() {
        return searched;
    }
    
    @EJB
    UserFacade uf;
    
    @EJB
    UserManager um;
    
    @EJB
    EventFacade ev;
    
    @PostConstruct
    public void init(){
        try {
            setEvents(um.getEventsVisibilityMasked(um.getLoggedUser().getId()));
        }
        catch (NotFoundException ex) { 
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSearched(String searched) {
        this.searched = searched;
    }

    public void search() {
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, resultsOutcome);
    }
    
    public List<User> getUsers(){
        return users;
    }
    public void setUsers(List<User> users){
        this.users = users;
    }
    
    public List<Event> getEvents(){
        return events;
    }
    public void setEvents(List<Event> events){
        this.events = events;
    }
    
}
