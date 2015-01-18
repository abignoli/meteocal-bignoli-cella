/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.dao.EventDAO;
import com.meteocal.business.dao.NotificationDAO;
import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.Notification;
import com.meteocal.business.entities.User;
import com.meteocal.business.entities.shared.EventStatus;
import com.meteocal.business.entities.shared.NotificationType;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.NotFoundException;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author USUARIO
 */
@Stateless
public class NotificationFacadeImplementation implements NotificationFacade {
    
    @EJB
    NotificationDAO notificationDAO;
    
    @EJB
    EventDAO eventDAO;

    public void createNotificationForEventChange(int eventID) throws NotFoundException {
        Notification changeNotification = new Notification();
        Event e = eventDAO.retrieve(eventID);
        
        changeNotification.setEvent(e);
        changeNotification.setType(NotificationType.EVENT_CHANGE);
        
        notificationDAO.save(changeNotification);
        
        linkParticipants(changeNotification);
    }

    private void linkParticipants(Notification notification) {
        for(User participant: notification.getEvent().getParticipants())
            linkParticipant(notification, participant);
    }

    private void linkParticipant(Notification notification, User participant) {
        notification.addNotificatedUser(participant);
    }


    public void createNotificationForEventCancel(int eventID) throws BusinessException {
        Notification cancelNotification = new Notification();
        Event e = eventDAO.retrieve(eventID);
        
        cancelNotification.setEvent(e);
        
        if(e.getStatus() != EventStatus.CANCELED)
            throw new BusinessException(BusinessException.INCONSISTENT_NOTIFICATION_CANCEL);
            
        cancelNotification.setType(NotificationType.EVENT_CANCEL);
        
        notificationDAO.save(cancelNotification);
        
        linkParticipants(cancelNotification);
    }

    
}
