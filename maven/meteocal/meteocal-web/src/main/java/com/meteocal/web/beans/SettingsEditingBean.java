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
import javax.annotation.PostConstruct;
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

    @EJB
    private UserFacade userFacade;
    
    @EJB
    private UserManager um;
    
    @Inject 
    private Cache session;
    
    private String confirmationPassword,previousPassword;
    private User editedUser;

        
    @PostConstruct
    public void init(){
        System.out.println("In Init");
        this.setEditedUser(um.getLoggedUser());
    }
    
    public User getEditedUser(){
        editedUser = um.getLoggedUser();    
        return this.editedUser;
    }
    
    public void setEditedUser(User u){
        this.editedUser = u;
    }
    
    public String getPreviousPassword(){
        return this.previousPassword;
    }
    
    public void setPreviousPassword(String value){
        this.previousPassword = value;
    }
    
    public String getConfirmationPassword(){
        return this.confirmationPassword;
    }
    
    public void setConfirmationPassword(String value){
        this.confirmationPassword = value;
    }    

    public String saveSettings(){
        try {
            
            if( editedUser.getPassword().length() == 0 ){
                System.out.println("Calling userFacade");
                userFacade.updateData(editedUser);
            }
            else{
                System.out.println("Second case: even the password was edited!");
                System.out.println("email: " + editedUser.getEmail() + " user " + editedUser.getUsername());
                System.out.println("old" + previousPassword + " new  " + editedUser.getPassword() + " conf " + this.confirmationPassword);
                
                if(confirmationPassword.equals(editedUser.getPassword())){
                    userFacade.updateData(editedUser ,previousPassword);
                }else{
                    session.setErrorType(true);
                    session.setErrorType(Dictionary.NOTMATCHEDPASSWORD);
                    return "/protected/personal/SettingsEdit";
                }
            }
            System.out.println("update done");
        }
        catch(EJBException e){
            System.out.println("CATCH EJB");
            session.setErrorType(true);
            session.setErrorType(Dictionary.EXCEPTION);
            return "/Error";
            
        }
        catch (BusinessException ex) {
            System.out.println("CATCH BusinessException");
            session.setErrorType(true);
            session.setErrorType(Dictionary.EXCEPTION);
            return "/Error";
            //Logger.getLogger(SettingsEditingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "/protected/personal/HomeCalendarMonth";
    }
}
