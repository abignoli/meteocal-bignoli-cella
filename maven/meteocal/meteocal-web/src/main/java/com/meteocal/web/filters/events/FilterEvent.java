/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.filters.events;

import com.meteocal.business.entities.User;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.security.UserManager;
import com.meteocal.business.shared.security.UserEventVisibility;
import static com.meteocal.business.shared.security.UserEventVisibility.CREATOR;
import static com.meteocal.business.shared.security.UserEventVisibility.VIEWER;
import com.meteocal.web.beans.component.ErrorBean;
import com.meteocal.web.exceptions.NotValidParameter;
import com.meteocal.web.utility.SYSO_Testing;
import com.meteocal.web.utility.SessionUtility;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Leo
 */
@ManagedBean
@RequestScoped
public class FilterEvent {

    @EJB
    private UserManager um;

    @Inject
    private SessionUtility sessionUtility;

    @Inject
    ErrorBean error;

    private User loggedUser;

    private HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

    private final String initialContext =  "Home";
    private final String errorOutcome = "Error";
    private final String creatorOutcome = "Creator";
    private final String viewerOutcome = "Viewer";
    private final String noVisibilityOutcome = "NoVisibility";

    @PostConstruct
    public void init() {
        this.setUser(um.getLoggedUser());
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    private void setUser(User user) {
        this.loggedUser = user;
    }

    public void check(ComponentSystemEvent event) {
        int eventID = -1;
        UserEventVisibility visibility;//one of: CREATOR, VIEWER, NO_VISIBILITY
        try {
            eventID = getID();
        }
        catch (NotValidParameter ex) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, errorOutcome);
        }

        SYSO_Testing.syso("FilteEvent. in EventPage: check");

        try {
            if (isNotLogged()) {
                SYSO_Testing.syso("I'm not logged");
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.getApplication().getNavigationHandler().handleNavigation(fc, null, initialContext);
            }
            else {
                String username = loggedUser.getUsername();
                try {
                    visibility = um.getVisibilityOverEvent(eventID);
                    sessionUtility.setParameter(eventID);

                    SYSO_Testing.syso("FilterEvent. Username " + username);
                    SYSO_Testing.syso("FilterEvent. I'm logged, and I've to check the visibility");
                    if (visibility == CREATOR) {
                        FacesContext fc = FacesContext.getCurrentInstance();
                        sessionUtility.setParameter(eventID);
                        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, creatorOutcome);
                    }
                    else {
                        if (visibility == VIEWER) {
                            FacesContext fc = FacesContext.getCurrentInstance();
                            sessionUtility.setParameter(eventID);
                            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, viewerOutcome);
                        }
                        else {// NO VISIBILITY
                            FacesContext fc = FacesContext.getCurrentInstance();
                            sessionUtility.setParameter(eventID);
                            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, noVisibilityOutcome);

                        }
                    }
                }
                catch (NotFoundException ex) {
                    error.setMessage("There is an incompatibility between you and the required event");
                    FacesContext fc = FacesContext.getCurrentInstance();
                    fc.getApplication().getNavigationHandler().handleNavigation(fc, null, errorOutcome);

                }
            }
        }
        catch (NullPointerException ec) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, errorOutcome);

        }
    }

    private boolean isNotLogged() {
        if (this.loggedUser == null) {
            return true;
        }
        return !sessionUtility.getLoggedUser().equals(this.loggedUser.getUsername());
    }

    private int getID() throws NotValidParameter {
        int id;
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
        else {
            id = sessionUtility.getParameterAsClient();
        }
        if (id < 0) {
            throw new NotValidParameter(NotValidParameter.MISSING_PARAMETER);
        }

        return id;
    }
}
