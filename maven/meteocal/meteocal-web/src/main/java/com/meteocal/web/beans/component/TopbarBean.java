/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.component;

import com.meteocal.business.security.UserManager;
import com.meteocal.web.utility.SYSO_Testing;
import com.meteocal.web.utility.SessionUtility;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
    private String notificationNumber,invitationNumber;
    
    @Inject
    private SessionUtility sessionUtility;
    
    @EJB
    UserManager um;
    
    @PostConstruct
    public void init() {
        SYSO_Testing.syso("topbar_init: ");
        setUsername(sessionUtility.getLoggedUser());
        setInvitationNumber(""+um.getNotSeenInvitationsCount());
        setNotificationNumber(""+um.getNotSeenNotificationsCount());
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

    public String getNotificationNumber() {
        return notificationNumber;
    }

    public String getInvitationNumber() {
        return invitationNumber;
    }

    public void setNotificationNumber(String notificatinNumber) {
        this.notificationNumber = notificatinNumber;
    }

    public void setInvitationNumber(String invitatioNumber) {
        this.invitationNumber = invitatioNumber;
    }
    
}
