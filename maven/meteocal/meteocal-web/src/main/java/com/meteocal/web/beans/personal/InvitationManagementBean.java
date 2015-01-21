/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.personal;

import com.meteocal.business.entities.Invitation;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@RequestScoped
@Named
public class InvitationManagementBean {

    List<Invitation> invitations;
    
    public List<Invitation> getNotifications(){
        return invitations;
    }
    
    public void setNotifications(List<Invitation> newInvitations){
        invitations = newInvitations;
    }
}
