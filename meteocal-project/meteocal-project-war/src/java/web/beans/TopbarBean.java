/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.beans;

import business.security.UserManager;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@Named
@RequestScoped
public class TopbarBean implements Serializable {
    
    private String username;
    
    @EJB
    private UserManager um;
    
    @PostConstruct
    public void init() {
        this.setUsername(um.getLoggedUser().getUsername());
    }
    
    public String getUsername(){
        return this.username;
    }
    
    private void setUsername(String user){
        System.out.println("user " + user );
        this.username = user;
    }

}
