/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.beans;

import business.entities.User;
import business.facade.UserFacade;
import business.security.UserManager;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author USUARIO
 */
@Named
@RequestScoped
//Is a Session Bean
public class RegistrationBean {
    
    @EJB
    UserManager um;
    

    private String passwordConfirmation;
    private User userToRegister;
    
    @PostConstruct
    public void init(){
        userToRegister = new User();
    }
    
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
            um.register(userToRegister);
            
            System.out.println("Registration complete!");
            //protected/personal/HomeCalendarMonth
            return "Index";
        }
        else 
            return "Error";
    }
}
