/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.filters;

import com.meteocal.business.entities.User;
import com.meteocal.business.security.UserManager;
import com.meteocal.web.utility.HttpUtility; 
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ComponentSystemEvent; 

/**
 *
 * @author Leo
 */
@ManagedBean
@RequestScoped
public class FilterEventCreator {
    @EJB
    private UserManager um; 

    private User loggedUser;
    private final String context = HttpUtility.getRequest().getContextPath();
    private final String indexPath = context + "/Index.xhtml";
    
    @PostConstruct
    public void init(){
        this.setUser(um.getLoggedUser());
    }
    
    private void setUser(User user){
        this.loggedUser = user;
    }
     
    public void check(ComponentSystemEvent event) {
        if(amIComingFromRedirect()){
            HttpUtility.getSession().setAttribute("comingFromRedirect", "no");
        }
        else{
            System.out.println("I'm not coming from redirect");
            HttpUtility.logout();
            HttpUtility.redirect(indexPath);
        }
    }

    private boolean logica() {
        int a = (int) (Math.random() * 10);
        return a % 2 == 0;
    }

    private boolean amIComingFromRedirect() {
        return HttpUtility.getSession().getAttribute("comingFromRedirect").equals("yes");
    }       
}
