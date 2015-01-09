/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.beans;

import business.entities.User;
import business.exceptions.BusinessException;
import business.facade.UserFacade;
import business.security.UserManager;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@Named
@RequestScoped
public class SettingsEditingBean implements Serializable{
    
    public SettingsEditingBean() {
    }
        
    @EJB
    private UserFacade userFacade;
    
    @EJB
    private UserManager um;
    
    private boolean calendarVisibility;
    private String previousPassword;
    private String newPassword;
    private String email="NULL";
    
    public boolean getCalendarVisibility(){
        return this.calendarVisibility;
    }
    
    public void setCalendarVisibility(boolean value){
        this.calendarVisibility = value;
    }
    
    public String getNewPassword(){
        return this.newPassword;
    }
    
    public void setNewPassword(String value){
        this.newPassword = value;
    }    

    public String getPreviousPassword(){
        return this.previousPassword;
    }
    
    public void setPreviousPassword(String value){
        this.previousPassword = value;
    }  
    
    public String getEmail(){
        return this.email;
    }
    
    public void setEmail(String value){
        this.email = value;
    }
    
    public String saveCalendarVisibility(){
            System.out.println("init saveVisibility");
            User localUser = new User();
            
            System.out.println("define localUser");
            localUser.setUsername(um.getLoggedUser().getUsername());
            localUser.setCalendarVisible(this.getCalendarVisibility());
            localUser.setEmail(um.getLoggedUser().getEmail());
            localUser.setPassword(um.getLoggedUser().getPassword());
            
            System.out.println("username: " + localUser.getUsername() + " visibility: " + localUser.isCalendarVisible() + " \nemail: " + localUser.getEmail() + " password: " + localUser.getPassword());
        
        try {
            
            System.out.println("Calling userFacade");
            userFacade.updateData(localUser);
            
            System.out.println("update done");
        }
        catch (BusinessException ex) {
            System.out.println("CATCH Business");
            System.out.println(ex.toString());
            System.out.println(ex.getLocalizedMessage());
            System.out.println(ex.getMessage());
            System.out.println(ex.initCause(ex));
            return "/Error";
        }
        catch(EJBException e){
            System.out.println("CATCH EJB");
            System.out.println(e.toString());
            return "/Error";
            
        }
        return "/protected/personal/HomeCalendarMonth";
    }
    
    public String saveAccountInformation(){
        User localUser = new User();
        
        
        localUser.setCalendarVisible(um.getLoggedUser().isCalendarVisible());
        
        localUser.setEmail(
                ("NULL".equals(this.email)) ? 
                        um.getLoggedUser().getEmail()
                        :
                        this.email
        );
        
        localUser.setPassword(this.newPassword);
        
        System.out.println(localUser.getUsername() + " " + localUser.isCalendarVisible() + " " + localUser.getEmail());
        try {
            userFacade.updateData( um.getLoggedUser() , this.previousPassword );
        }
        catch (BusinessException ex) {
            return "/Error";
        }
        
        return "/protected/personal/HomeCalendarMonth";
    }
}
