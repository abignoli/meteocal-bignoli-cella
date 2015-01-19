/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans;

import com.meteocal.business.entities.User;
import com.meteocal.business.facade.UserFacade;
import com.meteocal.business.security.UserManager; 
import com.meteocal.web.utility.SessionUtility;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author USUARIO
 */
@ManagedBean
@RequestScoped
public class RegistrationBean implements Serializable{
 
        
    @PostConstruct
    public void init(){
        System.out.println("init-RegistrationBean");
        setUser(new User());
    }
    
    @EJB
    UserManager um;
    
    @Inject
    SessionUtility sessionUtility;
    
    private String passwordConfirmation;
    private User userToRegister;

    
    /**
     * Creates a new instance of LoginBean
     */
    public RegistrationBean() {
    }
    
    public String getPasswordConfirmation(){
        return this.passwordConfirmation;
    }
        
    public void setPasswordConfirmation(String passwordConf){
        this.passwordConfirmation = passwordConf;
    }

    public User getUser() {
        return userToRegister;
    }

    public void setUser(User user) {
        this.userToRegister = user;
    }
    
    private boolean passwordMatching(){
        return userToRegister.getPassword().equals(this.passwordConfirmation);
    }
    
    public String register(){
        if( passwordMatching() ){
            System.out.println("Starting registration!");
            System.out.println("name: " + userToRegister.getUsername() + " psw: " + userToRegister.getPassword());
            //I've to use a try-catch or a boolean function in order to check if the username is available
            um.register(userToRegister);
            System.out.println("Registration complete!");
            return "Index";
        }
        else 
            return "Error";
    }
}
