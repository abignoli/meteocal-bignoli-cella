/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.utility;

import com.meteocal.business.entities.User;
import com.meteocal.business.security.UserManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
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
public class FilterBean_StructureTest implements Serializable {

    @EJB
    private UserManager um; 

    private User loggedUser;
    private final String indexPath = "/Index.xhtml";
    
    @PostConstruct
    public void init(){
        this.setUser(um.getLoggedUser());
    }
    
    private void setUser(User user){
        this.loggedUser = user;
    }
    
    public void isAdmin(ComponentSystemEvent event) {
        SYSO_Testing.syso("in isAdmin");
        HttpServletRequest request =  (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        

        String username = um.getLoggedUser().getUsername();
        SYSO_Testing.syso("Username " + loggedUser.getUsername());
        
        String relativePath = request.getRequestURI().replace("/meteocal-web", "");
        SYSO_Testing.syso("URI: " + request.getRequestURI());
        SYSO_Testing.syso("relativePath: " + relativePath);
        
        try {
            if (isNotLogged()) {
                SYSO_Testing.syso("I'm not logged");
                    request.getRequestDispatcher(indexPath).forward(request, response);
                SYSO_Testing.syso("somethin wrong!!");
            }
            else {
                SYSO_Testing.syso("I'm logged, and I've to check the visibility");
                if (logica()) {
                    request.getRequestDispatcher(relativePath).forward(request, response);
                }
                else {
                    request.getRequestDispatcher(relativePath).forward(request, response);
                }
            }
        }
        catch (ServletException ex) {
            Logger.getLogger(FilterBean_StructureTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(FilterBean_StructureTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean logica() {
        int a = (int) (Math.random() * 10);
        return a % 2 == 0;
    }

    private boolean isNotLogged() {
        return logica();
    }
}
