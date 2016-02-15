/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.filters.events;

import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.security.UserManager;
import com.meteocal.business.shared.security.UserEventVisibility;
import static com.meteocal.business.shared.security.UserEventVisibility.NO_VISIBILITY;
import com.meteocal.web.exceptions.NotValidParameter;
import com.meteocal.web.utility.SYSO_Testing;
import com.meteocal.web.utility.SessionUtility;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Leonardo Cella
 */
public class FilterEventNoVisibility {
        
    UserEventVisibility visibility;//one of: CREATOR, VIEWER, NO_VISIBILITY
    
    @Inject
    private SessionUtility sessionUtility;

    @EJB
    private UserManager um;
    
    private String loggedUser;
    HttpServletRequest request =  (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        
    private final String context = request.getContextPath();
    private final String errorPath = "Error.xhtml";
    
    @PostConstruct
    public void init(){
        this.setUser(sessionUtility.getLoggedUser());        
        request =  (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
       
    }
    
    private void setUser(String user){
        this.loggedUser = user;
    }
     
    public void check(ComponentSystemEvent event) {
        int eventID = -1;
        try {
            eventID = getID();
        }
        catch (NotValidParameter ex) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, errorPath);
            return;
        }
        
        if (isNotLogged()) {
            SYSO_Testing.syso("I'm not logged");
            FacesContext fc = FacesContext.getCurrentInstance();
            sessionUtility.setParameter(eventID);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, errorPath);
            return;
        }
        else {
            try {
                visibility = um.getVisibilityOverEvent(eventID);
                if (visibility != NO_VISIBILITY) {
                    FacesContext fc = FacesContext.getCurrentInstance();
                    fc.getApplication().getNavigationHandler().handleNavigation(fc, null, errorPath);
                    return;
                }
                //If I reach this code, I'm the creator

            }
            catch (NotFoundException ex) {
                Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ex);
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.getApplication().getNavigationHandler().handleNavigation(fc, null, errorPath);
                return;
            }
        }
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

    private boolean isNotLogged() {
        if (this.loggedUser == null) {
            return true;
        }
        return !sessionUtility.getLoggedUser().equals(this.loggedUser);
    }
}
    
