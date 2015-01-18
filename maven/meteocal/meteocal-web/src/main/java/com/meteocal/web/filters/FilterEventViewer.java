/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.filters;

import com.meteocal.business.entities.User;
import com.meteocal.business.security.UserManager;
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
public class FilterEventViewer {
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
     
    public void check(ComponentSystemEvent event) {
        System.out.println("in isAdmin");
        HttpServletRequest request = HttpUtility.getRequest();
        HttpServletResponse response = HttpUtility.getResponse();

        String username = um.getLoggedUser().getUsername();
        System.out.println("Username " + loggedUser.getUsername());
        
        String relativePath = request.getRequestURI().replace("/meteocal-web", "");
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("relativePath: " + relativePath);
        
        try {
            if (isNotLogged()) {
                System.out.println("I'm not logged");
                    request.getRequestDispatcher(indexPath).forward(request, response);
                System.out.println("somethin wrong!!");
            }
            else {
                System.out.println("I'm logged, and I've to check the visibility");
                if (logica()) {
                    request.getRequestDispatcher(relativePath).forward(request, response);
                }
                else {
                    request.getRequestDispatcher(relativePath).forward(request, response);
                }
            }
        }
        catch (ServletException ex) {
            Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ex);
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
