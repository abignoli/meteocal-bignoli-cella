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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.ScheduleEvent;

/**
 *
 * @author Leo
 */
@ManagedBean
@ViewScoped
public class OnEventSelectListener {

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
        String eventPath, strID;
        int eventID;
        selectedEvent = (ScheduleEvent) selectEvent.getObject();
        eventID = Integer.parseInt(selectedEvent.getData().toString());
        String username = sessionUtility.getLoggedUser();

        if (ef.find(eventID).isPrivateEvent()) {// NO VISIBILITY
            FacesContext fc = FacesContext.getCurrentInstance();
            sessionUtility.setParameter(eventID);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, noVisibilityOutcome);
            return;
        }
        try {
            visibility = um.getVisibilityOverEvent(eventID);
            sessionUtility.setParameter(eventID);
            SYSO_Testing.syso("FilterEvent. Username " + username);
            SYSO_Testing.syso("FilterEvent. I'm logged, and I've to check the visibility");
            if (visibility == CREATOR) {
                FacesContext fc = FacesContext.getCurrentInstance();
                sessionUtility.setParameter(eventID);
                fc.getApplication().getNavigationHandler().handleNavigation(fc, null, creatorOutcome);
                return;
            }
            else {
                if (visibility == VIEWER) {
                    FacesContext fc = FacesContext.getCurrentInstance();
                    sessionUtility.setParameter(eventID);
                    fc.getApplication().getNavigationHandler().handleNavigation(fc, null, viewerOutcome);
                    return;
                }
                else {// NO VISIBILITY
                    FacesContext fc = FacesContext.getCurrentInstance();
                    sessionUtility.setParameter(eventID);
                    fc.getApplication().getNavigationHandler().handleNavigation(fc, null, noVisibilityOutcome);
                    return;
                }
            }
        }
        catch (NotFoundException ex) {
            error.setMessage("There is an incompatibility between you and the required event");
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, errorOutcome);

        }
        return;
    }
}
