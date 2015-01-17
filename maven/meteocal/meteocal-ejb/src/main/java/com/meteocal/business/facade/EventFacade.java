/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.entities.Event;
import com.meteocal.business.exceptions.BusinessException;

/**
 *
 * @author USUARIO
 */

public interface EventFacade {
    
    public abstract Event create(Event e);
    
    public abstract Event find(int eventID);
    
    public abstract void addParticipant(int eventID, int userID) throws BusinessException;
    
    public abstract void removeParticipant(int eventID, int userID) throws BusinessException;
    
    public abstract void toggleParticipant(int eventID, int userID) throws BusinessException;
    
    public abstract void updateScheduling(int eventID, int start, int end) throws BusinessException;
    
    public abstract void updateData(Event e) throws BusinessException;
    
    
}
