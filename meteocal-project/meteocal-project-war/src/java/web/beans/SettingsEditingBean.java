/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.beans;

import java.beans.*;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Leo
 */
@ManagedBean 
@SessionScoped
public class SettingsEditingBean implements Serializable {

    private String privacyMod;
    private String previousPassword;
    private String newPassword;
    private String email;
    
    public SettingsEditingBean() {
    }
    
    public String getPrivacyMod(){
        return this.privacyMod;
    }
    
    public void setPrivacyMod(String value){
        this.privacyMod = value;
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
}
