/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.beans;

import business.entities.User;
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
public class SettingsBean implements Serializable {
    private String username;
    private String email;
    private boolean privacyMod;
    
    @EJB
    UserManager um;

    @PostConstruct
    public void init(){
        User u = um.getLoggedUser();
        this.username = u.getUsername();
        this.setEmail(u.getEmail());
        this.setPrivacyMod(u.isCalendarVisible());
    }
    
    public void setUsername(String user){
        this.username = user;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
       
    public void setPrivacyMod(boolean privacyMod){
        this.privacyMod = privacyMod;
    }
    
    public String getUsername(){
        return this.username;
    }
    public String getEmail(){
        return this.email;
    }
    public boolean getPrivacyMod(){
        return this.privacyMod;
    }
    
}
