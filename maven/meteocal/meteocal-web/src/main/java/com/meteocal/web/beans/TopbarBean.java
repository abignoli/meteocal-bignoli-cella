/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans;

import com.meteocal.web.utility.SYSO_Testing;
import com.meteocal.web.utility.SessionUtility;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@Named
@RequestScoped
public class TopbarBean implements Serializable {
    
    private String username;
    
    @Inject
    private SessionUtility sessionUtility;
    
    @PostConstruct
    public void init() {
        SYSO_Testing.syso("topbar_init: " + sessionUtility.getLoggedUser());
        SYSO_Testing.syso("it's setting:" + sessionUtility.getLoggedUser());
        this.setUsername(sessionUtility.getLoggedUser());
        SYSO_Testing.syso("now the field has value: " + this.getUsername());
    }
    
    public String getUsername(){
        SYSO_Testing.syso("topbarBean_getter: " + username );
        return this.username;
    }
    
    private void setUsername(String user){
        SYSO_Testing.syso("topbarBean_setter: " + user );
        this.username = user;
    }

}
