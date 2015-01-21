/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.dao.InvitationDao;
import com.meteocal.business.entities.keys.InvitationID;
import com.meteocal.business.exceptions.NotFoundException;
import javax.ejb.EJB;

/**
 *
 * @author USUARIO
 */
public class InvitationFacadeImplementation implements InvitationFacade {
    
    @EJB
    InvitationDao invitationDAO;

    @Override
    public void setAsSeen(InvitationID invitationID) throws NotFoundException {
        invitationDAO.retrieve(invitationID).setSeen(true);
    }
    
}
