/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.personal;

import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.business.security.UserManager;
import com.meteocal.business.shared.security.UserEventVisibility;
import static com.meteocal.business.shared.security.UserEventVisibility.CREATOR;
import static com.meteocal.business.shared.security.UserEventVisibility.VIEWER;
import com.meteocal.web.beans.component.ErrorBean;
import com.meteocal.web.utility.SYSO_Testing;
import com.meteocal.web.utility.SessionUtility;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.ScheduleEvent;

/**
 *
 * @author Leo
 */
@ManagedBean
@ViewScoped
public class OnEventListenerUser {

    public static final String creatorOutcome = "Creator";
    public static final String errorOutcome = "Error";
    public static final String viewerOutcome = "Viewer";
    public static final String noVisibilityOutcome = "noVisibility";
 
    @Inject
    SessionUtility sessionUtility;

    @Inject
    ErrorBean error;

    @EJB
    EventFacade ef;

    @EJB
    UserManager um;

    private ScheduleEvent selectedEvent;

    @PostConstruct
    public void init() {
        
    }

    public void onEventSelect(SelectEvent selectEvent) {
        UserEventVisibility visibility;//one of: CREATOR, VIEWER, NO_VISIBILITY
        int eventID;
        selectedEvent = (ScheduleEvent) selectEvent.getObject();
        eventID = Integer.parseInt(selectedEvent.getData().toString());
        String username = sessionUtility.getLoggedUser();

        try {
            visibility = um.getVisibilityOverEvent(eventID);
            sessionUtility.setParameter(eventID);
            SYSO_Testing.syso("FilterEvent. Username " + username);
            SYSO_Testing.syso("FilterEvent. I'm logged, and I've to check the visibility");
            if (visibility == CREATOR) {
                FacesContext fc = FacesContext.getCurrentInstance();
                sessionUtility.setParameter(eventID);
                //fc.getApplication().getNavigationHandler().handleNavigation(fc, null, creatorOutcome);
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String contextPath = request.getContextPath();
                FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/protected/event/EventPageCreator.xhtml?faces-redirect=true");
                
                return;
            }
            else {
                if (visibility == VIEWER && !ef.find(eventID).isPrivateEvent()) {
                    FacesContext fc = FacesContext.getCurrentInstance();
                    sessionUtility.setParameter(eventID);
                    //fc.getApplication().getNavigationHandler().handleNavigation(fc, null, viewerOutcome);
                    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                    String contextPath = request.getContextPath();
                    FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/protected/event/EventPageViewer.xhtml?faces-redirect=true");
                    
                    return;
                }
                else {// NO VISIBILITY
                    FacesContext fc = FacesContext.getCurrentInstance();
                    sessionUtility.setParameter(eventID);
                    //fc.getApplication().getNavigationHandler().handleNavigation(fc, null, noVisibilityOutcome);
                    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                    String contextPath = request.getContextPath();
                    FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/protected/event/EventPageNoVisibility.xhtml?faces-redirect=true");
                
                    return;
                }
            }
        }
        catch (NotFoundException ex) {
            error.setMessage("There is an incompatibility between you and the required event");
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, errorOutcome);
        }
        catch (IOException ex) {
            Logger.getLogger(OnEventListenerUser.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, errorOutcome);
        }
        return; 
    }
}
