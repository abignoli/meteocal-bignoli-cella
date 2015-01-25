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
import com.meteocal.business.shared.utils.LocalDateTimeUtils;
import com.meteocal.shared.ServiceVariables;
import java.time.LocalDateTime;
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
        Event e = eventDAO.retrieve(eventID);
        
        if(inRangeForWeatherNotification(e)) {
            Notification changeNotification = new Notification();

            changeNotification.setEvent(e);
            changeNotification.setType(NotificationType.EVENT_CHANGE);

            save(changeNotification);

            notificateParticipants(changeNotification);
        }
    }

    private void notificateParticipantsAndCreator(Notification notification) {
        notificateCreator(notification);
        notificateParticipants(notification);
    }
    
    private void notificateCreator(Notification notification) {
        notificateUser(notification, notification.getEvent().getCreator());
    }
    
    private void notificateParticipants(Notification notification) {        
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

        save(cancelNotification);

        notificateParticipants(cancelNotification);
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
        
        save(weatherNotification);

        notificateParticipantsAndCreator(weatherNotification);
    }

    private void save(Notification notification) {
        notification.setGenerationDateTime(LocalDateTime.now());
        
        notificationDAO.save(notification);
    }

    private boolean inRangeForWeatherNotification(Event e) {
        LocalDateTime max = LocalDateTime.now().minusHours(LocalDateTime.now().getHour()).plusDays(ServiceVariables.WEATHER_NOTIFICATION_RANGE_DAYS_BEFORE_START + 1);
        
        return e.getStart().isBefore(max);
    }

    @Override
    public void createNotificationForSuggestedChange(int eventID) throws NotFoundException {
        Notification changeSuggestionNotification = new Notification();
        Event e = eventDAO.retrieve(eventID);

        changeSuggestionNotification.setEvent(e);
        changeSuggestionNotification.setType(NotificationType.SUGGESTED_CHANGE);
        
        save(changeSuggestionNotification);

        notificateCreator(changeSuggestionNotification);
    }

}
