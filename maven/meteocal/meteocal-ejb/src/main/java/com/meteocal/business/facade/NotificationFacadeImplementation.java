/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.dao.EventDAO;
import com.meteocal.business.dao.NotificationDAO;
import com.meteocal.business.dao.NotificationViewDAO;
import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.Notification;
import com.meteocal.business.entities.User;
import com.meteocal.business.entities.keys.NotificationViewID;
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
    NotificationViewDAO notificationViewDAO;

    @EJB
    EventDAO eventDAO;

    public void createNotificationForEventChange(int eventID) throws NotFoundException {
        Notification changeNotification = new Notification();
        Event e = eventDAO.retrieve(eventID);

        changeNotification.setEvent(e);
        changeNotification.setType(NotificationType.EVENT_CHANGE);

        notificationDAO.save(changeNotification);

        notificateParticipantsAndCreator(changeNotification);
    }

    private void notificateParticipantsAndCreator(Notification notification) {
        // Notificate Creator
        notificateUser(notification, notification.getEvent().getCreator());
        
        for (User participant : notification.getEvent().getParticipants()) {
            notificateUser(notification, participant);
        }
    }

    private void notificateUser(Notification notification, User participant) {
        notification.addNotificatedUser(participant);
    }

    public void createNotificationForEventCancel(int eventID) throws BusinessException {
        Notification cancelNotification = new Notification();
        Event e = eventDAO.retrieve(eventID);

        cancelNotification.setEvent(e);

        if (e.getStatus() != EventStatus.CANCELED) {
            throw new BusinessException(BusinessException.INCONSISTENT_NOTIFICATION_CANCEL);
        }

        cancelNotification.setType(NotificationType.EVENT_CANCEL);

        notificationDAO.save(cancelNotification);

        notificateParticipantsAndCreator(cancelNotification);
    }

    @Override
    public void setAsSeen(int userID, int notificationID) throws NotFoundException {
        NotificationViewID notificationViewKey = new NotificationViewID(userID, notificationID);

        notificationViewDAO.retrieve(notificationViewKey).setSeen(true);
    }

    @Override
    public void createNotificationForWeatherConditions(int eventID, boolean newForecastsGood) throws NotFoundException {
        Notification weatherNotification = new Notification();
        Event e = eventDAO.retrieve(eventID);

        weatherNotification.setEvent(e);
        weatherNotification.setType(NotificationType.WEATHER_NOTIFICATION);
        weatherNotification.setGoodWeather(newForecastsGood);
        
        notificationDAO.save(weatherNotification);

        notificateParticipantsAndCreator(weatherNotification);
    }

}
