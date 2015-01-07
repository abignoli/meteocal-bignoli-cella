/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.beans;

import java.beans.*;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@Named
@SessionScoped
public class UserSessionBean implements Serializable {
    private String username;
    private String prop="casa";
    
    @PostConstruct
    public void init(){
        username = FacesContext.getCurrentInstance().getAttributes().get("username").toString();
    }
    
    public String getUsername(){
        return this.username;
    }
            
    public String getProp(){
        return this.prop;
    }
    
    public void setUsername(String user){
        this.username = user;
    }
}
