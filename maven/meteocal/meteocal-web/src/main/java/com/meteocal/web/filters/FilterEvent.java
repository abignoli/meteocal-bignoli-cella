/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.filters;

import com.meteocal.business.entities.User;
import com.meteocal.business.security.UserManager;
import com.meteocal.business.shared.security.UserEventVisibility;
import static com.meteocal.business.shared.security.UserEventVisibility.CREATOR;
import static com.meteocal.business.shared.security.UserEventVisibility.VIEWER;
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
import javax.servlet.ServletException;
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
    
    HttpServletRequest request =  (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        
    private final String context = request.getContextPath();
    private final String indexPath = context + "/Index.xhtml";
    private final String creatorPath = context + "/protected/event/EventPageCreator.xhtml";
    private final String viewerPath = context + "/protected/event/EventPageViewer.xhtml";
    private final String noVisibilityPath = context + "/protected/event/EventPageNoVisibility.xhtml";

    @PostConstruct
    public void init() {
        this.setUser(um.getLoggedUser());
        request =  (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        
    }

    private void setUser(User user) {
        this.loggedUser = user;
    }

    public void check(ComponentSystemEvent event) {
        //eventID = SessionUtility.getQueryString().split("=")[1];
        int eventID = 1;
        boolean comingFromRedirect;
        UserEventVisibility visibility;//one of: CREATOR, VIEWER, NO_VISIBILITY

        System.out.println("in EventPage: check");
        String relativePath = request.getRequestURI().replace("/meteocal-web", "");

        System.out.println("URI: " + request.getRequestURI());
        System.out.println("relativePath: " + relativePath);

        try {

            if (isNotLogged()) {
                System.out.println("I'm not logged");
                response.sendRedirect(indexPath);
                System.out.println("something wrong!!");
            }
            else {
                String username = loggedUser.getUsername();
//                try {
//                    visibility = um.getVisibilityOverEvent(eventID);
//                }
//                catch (NotFoundException ex) {
//                    Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ex);
//                    SessionUtility.redirect("/Index.xhtml");
//                }
                visibility = CREATOR;
                System.out.println("Username " + username);
                System.out.println("I'm logged, and I've to check the visibility");
                if (visibility == CREATOR) {
                    System.out.println("creator");
                    sessionUtility.setComingFromDispatcher();
                    sessionUtility.setEventID(eventID);
                    try {
                        System.out.println("pre-dispatcher");
                        response.sendRedirect(creatorPath);
                        System.out.println("post-dispatcher");
                    }
                    catch (IOException ex) {
                        System.out.println("secondTryCatch");
                        Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else {
                    if (visibility == VIEWER) {
                        System.out.println("viewer");
                        sessionUtility.setComingFromDispatcher();
                        request.getRequestDispatcher(viewerPath).forward(request, response);
                    }
                    else {// NO VISIBILITY
                        System.out.println("no Visibility");
                        sessionUtility.setComingFromDispatcher();
                        request.getRequestDispatcher(noVisibilityPath).forward(request, response);  
                    }
                } 
            }
        }
        catch (NullPointerException ec) {
            Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ec);
        }
        catch (IOException ex) {
            Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ServletException ex) {
            Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isNotLogged() {
        return !sessionUtility.getLoggedUser().equals(this.loggedUser.getUsername());
    }
}
