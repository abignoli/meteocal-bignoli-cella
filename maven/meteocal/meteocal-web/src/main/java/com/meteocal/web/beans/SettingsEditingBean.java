/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans;

import com.meteocal.business.entities.User;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.facade.UserFacade;
import com.meteocal.business.security.UserManager;
import com.meteocal.web.utility.Cache;
import com.meteocal.web.utility.Dictionary;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
    
    @Inject 
    Cache session;
    
    private String password;
    private String previousPassword;
    private User user = um.getLoggedUser(); 
    
    public User getUser(){
        return this.user;
    }
    
    public void setUser(User u){
        this.user = u;
    }
    
    public String getPreviousPassword(){
        return this.previousPassword;
    }
    
    public void setPreviousPassword(String value){
        this.previousPassword = value;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public void setPassword(String value){
        this.password = value;
    }    

    public String saveSettings(){
            System.out.println("init saveSettings");
            User localUser;
            
            System.out.println("define localUser");
            localUser = um.getLoggedUser();
            localUser.setUsername(um.getLoggedUser().getUsername());
            localUser.setEmail(um.getLoggedUser().getEmail());
            localUser.setPassword(um.getLoggedUser().getPassword());
            localUser.setId(um.getLoggedUser().getId());
            localUser.setCalendarVisible(um.getLoggedUser().isCalendarVisible());
            localUser.setInvitations(um.getLoggedUser().getInvitations());
            localUser.setNotificationViews(um.getLoggedUser().getNotificationViews());
            localUser.setNotifications(um.getLoggedUser().getNotifications());
            localUser.setParticipatingTo(um.getLoggedUser().getParticipatingTo());
            localUser.setInvitedTo(um.getLoggedUser().getInvitedTo());
            localUser.setCreatedEvents(um.getLoggedUser().getCreatedEvents());
            localUser.setGroupName(um.getLoggedUser().getGroupName());
            
            System.out.println("username: " + localUser.getUsername() + " visibility: " + localUser.isCalendarVisible() + " \nemail: " + localUser.getEmail() + " password: " + localUser.getPassword());

        try {
            
            if( this.password.length() == 0 ){
                System.out.println("Calling userFacade");
                userFacade.updateData(localUser);
            }
            else{
                System.out.println("Second case: even the password was edited");
                
                if(this.password.equals(um.getLoggedUser().getPassword())){
                    userFacade.updateData(localUser,previousPassword);
                }else{
                    session.setErrorType(true);
                    session.setErrorType(Dictionary.NOTMATCHEDPASSWORD);
                    return "/protected/personal/SettingsEdit";
                }
            }
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
}
