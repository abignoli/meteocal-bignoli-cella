/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.renders;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Leonardo Cella
 */
@SessionScoped
@ManagedBean
public class IndexRender implements Serializable{
    private boolean errorLogin,errorRegistration;
    private String errorLoginDetails,errorRegistrationDetails;

    public boolean isErrorRegistration() {
        return errorRegistration;
    }

    public void setErrorRegistration(boolean errorRegistration) {
        this.errorRegistration = errorRegistration;
    }

    public String getErrorRegistrationDetails() {
        return errorRegistrationDetails;
    }

    public void setErrorRegistrationDetails(String errorRegistrationDetails) {
        this.errorRegistrationDetails = errorRegistrationDetails;
    }
    
    public boolean getErrorLogin(){
        return errorLogin;
    }
    
    public void setErrorLogin(boolean newValue){
        errorLogin = newValue;
    }
    
    public void setErrorDetails(String err){
        errorLoginDetails = err;
    }
    
    public String getErrorDetails(){
        return errorLoginDetails;
    }
}
