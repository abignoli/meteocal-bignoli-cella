/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.entities.Event;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.NotFoundException;

/**
 *
 * @author USUARIO
 */
public interface NotificationFacade {
    public void createNotificationForEventChange(int eventID) throws NotFoundException;
    public void createNotificationForEventCancel(int eventID) throws BusinessException;
    public void setAsSeen(int userID, int notificationID) throws NotFoundException;
}
