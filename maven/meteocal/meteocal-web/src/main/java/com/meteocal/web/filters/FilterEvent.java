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
import static com.meteocal.business.shared.security.UserEventVisibility.NO_VISIBILITY;
import static com.meteocal.business.shared.security.UserEventVisibility.VIEWER;
import com.meteocal.web.utility.HttpUtility;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ComponentSystemEvent;
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

    private User loggedUser;
    
    private final String context = HttpUtility.getRequest().getContextPath();
    private final String indexPath = context + "/Index.xhtml";
    private final String creatorPath = context + "/Index.xhtml";
    private final String viewerPath = context + "/Index.xhtml";
    private final String noVisibilityPath = context + "/Index.xhtml";

    @PostConstruct
    public void init() {
        this.setUser(um.getLoggedUser());
    }

    private void setUser(User user) {
        this.loggedUser = user;
    }

    public void check(ComponentSystemEvent event) {
        //eventID = HttpUtility.getQueryString().split("=")[1];
        int eventID = 1; 
        boolean comingFromRedirect;
        UserEventVisibility visibility;//one of: CREATOR, VIEWER, NO_VISIBILITY

        System.out.println("in EventPage: check");
        HttpServletRequest request = HttpUtility.getRequest();
        HttpServletResponse response = HttpUtility.getResponse();
        String relativePath = request.getRequestURI().replace("/meteocal-web", "");

        System.out.println("URI: " + request.getRequestURI());
        System.out.println("relativePath: " + relativePath);

        try {

            if (isNotLogged()) {
                System.out.println("I'm not logged");
                HttpUtility.redirect(indexPath);
                System.out.println("something wrong!!");
            }
            else {
                String username = um.getLoggedUser().getUsername();
//                try {
//                    visibility = um.getVisibilityOverEvent(eventID);
//                }
//                catch (NotFoundException ex) {
//                    Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ex);
//                    HttpUtility.redirect("/Index.xhtml");
//                }
                comingFromRedirect = HttpUtility.getSession().getAttribute("comingFromRedirect").equals("yes");
                visibility = CREATOR;
                System.out.println("Username " + loggedUser.getUsername());
                System.out.println("I'm logged, and I've to check the visibility");
                if (comingFromRedirect) {
                    if (visibility == CREATOR) {
                        System.out.println("creator");
                        HttpUtility.getSession().setAttribute("comingFromRedirect", "yes");
                        HttpUtility.getSession().setAttribute("eventID", eventID);
                        HttpUtility.dispatcher(creatorPath);
                    }
                    else {
                        if (visibility == VIEWER) {
                            System.out.println("viewer");
                            HttpUtility.getSession().setAttribute("comingFromRedirect", "yes");
                            HttpUtility.dispatcher(viewerPath);
                        }
                        else {// NO VISIBILITY
                            System.out.println("no Visibility");
                            HttpUtility.getSession().setAttribute("comingFromRedirect", "yes");
                            HttpUtility.dispatcher(noVisibilityPath);
                        }
                    }
                }
                else {//Where are you coming from?
                    HttpUtility.redirect("/protected/personal/HomeCalendarMonth.xhtml");
                }
            }
        }
        catch (NullPointerException ec) {
            Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ec);
        }
    }

    private boolean isNotLogged() {
        return !HttpUtility.getSession().getAttribute("logged").equals("yes");
    }
}
