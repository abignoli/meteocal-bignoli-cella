/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.personal;

import com.meteocal.business.entities.Invitation;
import com.meteocal.business.entities.keys.InvitationID;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.business.facade.InvitationFacade;
import com.meteocal.business.security.UserManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@RequestScoped
@Named
public class InvitationManagementBean {
    private int eventID,userID;
    
    @EJB
    private EventFacade eventFacade;

    @EJB
    private InvitationFacade invitationFacade;

    @EJB
    private UserManager userManager;
    
    @PostConstruct
    public void init() {
        setInvitations(userManager.getCurrentInvitations());
    }

    List<Invitation> invitations;

    public List<Invitation> getInvitations() {
        for (Invitation i : invitations) {
            try {
                userManager.setInvitationAsSeen(i.getID());
            }
            catch (NotFoundException ex) {
                Logger.getLogger(InvitationManagementBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return invitations;
    }

    public void decline() throws NotFoundException{
        getParam();
        InvitationID invID =  new InvitationID(userID,eventID);
        userManager.setInvitationAsDeclined(invID);
    }
    
    public void setInvitations(List<Invitation> newInvitations) {
        invitations = newInvitations;
    }

    public void accept() throws BusinessException {
        getParam();
        eventFacade.addParticipant(eventID, userID);
    }
    
    private void getParam(){
        String evID;
        evID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("eventID");
        eventID = Integer.parseInt(evID);
        userID = userManager.getLoggedUser().getId();
    }
    

}
