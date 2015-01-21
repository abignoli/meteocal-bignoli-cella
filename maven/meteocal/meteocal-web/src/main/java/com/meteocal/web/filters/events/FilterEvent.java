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
import com.meteocal.web.utility.SYSO_Testing;
import com.meteocal.web.utility.SessionUtility;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private User loggedUser;

    private HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    private HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

    private final String context = request.getContextPath();
    private final String initialContext = context + "/protected/personal/HomeCalendarMonth.xhtml";
    private final String errorPath = context + "/Error.xhtml";
    private final String creatorPath = context + "/protected/event/EventPageCreator.xhtml?eventID=";
    private final String viewerPath = context + "/protected/event/EventPageViewer.xhtml?eventID=";
    private final String noVisibilityPath = context + "/protected/event/EventPageNoVisibility.xhtml?eventID=";

    @PostConstruct
    public void init() {
        this.setUser(um.getLoggedUser());
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

    }

    private void setUser(User user) {
        this.loggedUser = user;
    }

    public void check(ComponentSystemEvent event) {
        int eventID;
        boolean comingFromRedirect;
        UserEventVisibility visibility;//one of: CREATOR, VIEWER, NO_VISIBILITY
        eventID = getID();
        
        SYSO_Testing.syso("FilteEvent. in EventPage: check");

        try {
            if (isNotLogged()) {
                SYSO_Testing.syso("I'm not logged");
                response.sendRedirect(errorPath);
                SYSO_Testing.syso("something wrong!!");
            }
            else {
                String username = loggedUser.getUsername();
                try {
                    if(eventID < 0){
                        SYSO_Testing.syso("FilterEvent. without parameter");
                        response.sendRedirect(initialContext);
                    }
                        
                    visibility = um.getVisibilityOverEvent(eventID);

                    SYSO_Testing.syso("FilterEvent. Username " + username);
                    SYSO_Testing.syso("FilterEvent. I'm logged, and I've to check the visibility");
                    if (visibility == CREATOR) {
                        SYSO_Testing.syso("FilterEvent. case:creator");
                        try {
                            SYSO_Testing.syso("FilterEvent. pre-redirect");
                            response.sendRedirect(creatorPath+eventID);
                        }
                        catch (IOException ex) {
                            SYSO_Testing.syso("FilterEvent. secondTryCatch");
                            Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else {
                        if (visibility == VIEWER) {
                            SYSO_Testing.syso("FilterEvent. case:viewer");
                            response.sendRedirect(viewerPath+eventID);
                        }
                        else {// NO VISIBILITY
                            SYSO_Testing.syso("FilterEvent. case:no Visibility");
                            response.sendRedirect(noVisibilityPath+eventID);
                        }
                    }
                }
                catch (NotFoundException ex) {
                    Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendRedirect(errorPath);
                }
            }
        }
        catch (NullPointerException ec) {
            SYSO_Testing.syso("FilterEvent. notFoundEvent");
            
        }
        catch (IOException ex) {
            Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isNotLogged() {
        if (this.loggedUser == null) {
            return true;
        }
        return !sessionUtility.getLoggedUser().equals(this.loggedUser.getUsername());
    }

    private int getID() {
        int id;
        String strID;
        strID = request.getParameter("eventID");
        id = Integer.parseInt(strID);
        return id;
    }
}
