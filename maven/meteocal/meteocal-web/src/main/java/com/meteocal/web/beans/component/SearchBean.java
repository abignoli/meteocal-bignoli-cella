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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Leo
 */
@ManagedBean
@SessionScoped
public class SearchBean implements Serializable {

    private String searched;
    private final String resultsOutcome = "/protected/SearchResults.xhtml";
    private List<User> users;
    private List<Event> events;

    @EJB
    UserFacade uf;

    @EJB
    UserManager um;
    
    @EJB
    EventFacade ev;

    @PostConstruct
    public void init() {
        events = new ArrayList<Event>();
        users = new ArrayList<User>();
    }

    public void setSearched(String searched) {
        this.searched = searched;
    }

    public String getSearched() {
        return searched;
    }

    public String search() {
        try {
            setEvents(um.getEventsVisibilityMasked(um.getLoggedUser().getId()));
            setUsers(uf.search(searched));
        } catch (NotFoundException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultsOutcome;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        if(this.users==null)
            this.users = new ArrayList<User>();
        for (User user : users) {
            this.users.add(user);
        }
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        if (this.events == null) {
            this.events = new ArrayList<Event>();
        }
        for (Event event : events) {
            this.events.add(event);
        }
    }

}
