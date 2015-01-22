/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.personal;

import com.meteocal.business.entities.Invitation;
import com.meteocal.business.security.UserManager;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@RequestScoped
@Named
public class InvitationManagementBean {
    
    @EJB
    UserManager um;
    
    @PostConstruct
    public void init(){
        setInvitations(um.getLoggedUser().getInvitations());
    }
    
    List<Invitation> invitations;
    
    public List<Invitation> getInvitations(){
        return invitations;
    }
    
    public void setInvitations(List<Invitation> newInvitations){
        invitations = newInvitations;
    }
}
