/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.personal;

import com.meteocal.web.utility.SessionUtility;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.ScheduleEvent;

/**
 *
 * @author Leo
 */
@ManagedBean
@ViewScoped
public class Listener {
    @Inject
    SessionUtility sessionUtility;
    
    private ScheduleEvent selectedEvent;
    private HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    private HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

    
    @PostConstruct
    public void init(){    
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

    }
        
    public void onEventSelect(SelectEvent selectEvent) {
        String eventPath;
        String eventID;
        selectedEvent = (ScheduleEvent) selectEvent.getObject();
        eventID = selectedEvent.getId();
        eventPath = "/protected/event/EventPage.xhtml?eventID=" + eventID;
        try {
            response.sendRedirect(eventPath);
        }
        catch (IOException ex) {
            Logger.getLogger(HomeMonthlyBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
