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
import com.meteocal.web.utility.SessionUtility;
import com.meteocal.web.utility.Dictionary;
import com.meteocal.web.utility.SYSO_Testing;
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
    private SessionUtility sessionUtility;
    
    private String confirmationPassword,previousPassword;
    private User editedUser;

        
    @PostConstruct
    public void init(){
        this.setEditedUser(um.getLoggedUser());
    }
    
    public User getEditedUser(){
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
                SYSO_Testing.syso("Calling userFacade");
                userFacade.updateData(editedUser);
            }
            else{
                SYSO_Testing.syso("Second case: even the password was edited!");
                SYSO_Testing.syso("email: " + editedUser.getEmail() + " user " + editedUser.getUsername());
                SYSO_Testing.syso("old" + previousPassword + " new  " + editedUser.getPassword() + " conf " + this.confirmationPassword);
                
                if(confirmationPassword.equals(editedUser.getPassword())){
                    userFacade.updateData(editedUser ,previousPassword);
                }else{
                    sessionUtility.setError(Dictionary.NOTMATCHEDPASSWORD);
                    return "/protected/personal/SettingsEdit";
                }
            }
            SYSO_Testing.syso("update done");
        }
        catch(EJBException e){
            SYSO_Testing.syso("CATCH EJB");
            sessionUtility.setError(Dictionary.EXCEPTION);
            return "/Error";
            
        }
        catch (BusinessException ex) {
            SYSO_Testing.syso("CATCH BusinessException");
            sessionUtility.setError(Dictionary.EXCEPTION);
            return "/Error";
            //Logger.getLogger(SettingsEditingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "/protected/personal/HomeCalendarMonth";
    }
}
