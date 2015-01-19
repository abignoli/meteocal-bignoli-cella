/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans;

import com.meteocal.business.security.UserManager;
import com.meteocal.web.utility.SessionUtility;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@Named
@RequestScoped
public class TopbarBean implements Serializable {
    
    private String username;
    
    @Inject
    private SessionUtility sessionUtility;
    
    @PostConstruct
    public void init() {
        this.setUsername(sessionUtility.getLoggedUser());
    }
    
    public String getUsername(){
        return this.username;
    }
    
    private void setUsername(String user){
        System.out.println("topbarBean: username " + user );
        this.username = user;
    }

}
