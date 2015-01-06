/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.beans;

import business.entities.User;
import business.facade.UserFacade;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author USUARIO
 */
@ManagedBean
@SessionScoped
//Is a Session Bean
public class RegistrationBean {
    
    @EJB
    UserFacade userFacade;
    

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
        if (userToRegister == null) {
            userToRegister = new User();
        }
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
            userFacade.save(userToRegister);
            System.out.println("Registration complete!");
            return "Index";
        }
        else 
            return "Error";
    }
}
