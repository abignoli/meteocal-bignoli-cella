/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.entities.keys.InvitationID;
import com.meteocal.business.exceptions.NotFoundException;

/**
 *
 * @author Andrea Bignoli
 */
public interface InvitationFacade {
    public void setAsSeen(InvitationID invitationID) throws NotFoundException;

    public void setAsDeclined(InvitationID invitationID) throws NotFoundException;
}
