/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.filters.events;

import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.security.UserManager;
import com.meteocal.business.shared.security.UserEventVisibility;
import static com.meteocal.business.shared.security.UserEventVisibility.CREATOR;
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
public class FilterEventCreator {
        
    UserEventVisibility visibility;//one of: CREATOR, VIEWER, NO_VISIBILITY
    
    @Inject
    private SessionUtility sessionUtility;

    @EJB
    private UserManager um;
    
    private String loggedUser;
    private HttpServletRequest request =  (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    private HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        
    private final String context = request.getContextPath();
    private final String errorPath = context + "/Error.xhtml";
    private final String indexPath = context + "/Index.xhtml";
    private final String initialContext = context + "/protected/personal/HomeCalendarMonth.xhtml";
    
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
        int eventID;
        eventID = getID();
        if(! sessionUtility.isThereAnActiveSession() ){
            try {
                response.sendRedirect(indexPath);
            }
            catch (IOException ex) {
                Logger.getLogger(FilterEventCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            try {
                visibility = um.getVisibilityOverEvent(eventID);
                if( visibility != CREATOR ){
                    try {
                        response.sendRedirect(errorPath);
                    }
                    catch (IOException ex1) {
                        Logger.getLogger(FilterEventCreator.class.getName()).log(Level.SEVERE, null, ex1);
                    }    
                }
            }
            catch (NotFoundException ex) {
                Logger.getLogger(FilterEventCreator.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    response.sendRedirect(errorPath);
                }
                catch (IOException ex1) {
                    Logger.getLogger(FilterEventCreator.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            
        }
    }

    private int getID() {
        int id;
        String strID;
        strID = request.getParameter("eventID");
        id = Integer.parseInt(strID);
        return id;
    }
}
