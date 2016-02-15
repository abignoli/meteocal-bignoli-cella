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
 * @author Andrea Bignoli
 */
public interface NotificationFacade {
    public void createNotificationForEventChange(int eventID) throws NotFoundException;
    public void createNotificationForEventCancel(int eventID) throws BusinessException;
    public void setAsSeen(int userID, int notificationID) throws NotFoundException;
    
    /**
     * Creates notifications about weather conditions if the service conditions are met. 
     * 
     * Namely if the event is scheduled ServiceVariables.WEATHER_NOTIFICATION_RANGE_DAYS_BEFORE_START (1) days from now.
     * 
     * @param eventID
     * @param newForecastsGood
     * @throws NotFoundException 
     */
    public void createNotificationForWeatherConditions(int eventID, boolean newForecastsGood) throws NotFoundException;

    public void createNotificationForSuggestedChange(int eventID) throws NotFoundException;
}
