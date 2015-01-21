/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.filters.events;

import com.meteocal.web.utility.SYSO_Testing;
import com.meteocal.web.utility.SessionUtility; 
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
    
    @Inject
    private SessionUtility sessionUtility;

    private String loggedUser;
    HttpServletRequest request =  (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        
    private final String context = request.getContextPath();
    private final String errorPath = context + "/Error.xhtml";
    
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
        if(amIComingFromDispatcher()){
            SYSO_Testing.syso("FilterEventCreator in this case, everything it's ok");
            sessionUtility.setNotComingFromDispatcher();
        }
        else{
            try {
                SYSO_Testing.syso("I'm not coming from redirect");
                sessionUtility.sessionLogout();
                response.sendRedirect(errorPath);
            }
            catch (IOException ex) {
                Logger.getLogger(FilterEventCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean logica() {
        int a = (int) (Math.random() * 10);
        return a % 2 == 0;
    }

    private boolean amIComingFromDispatcher() {
        if(this.loggedUser==null)return false;
        return sessionUtility.getComingFromDispatcher();
    }
}