/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.tests.AB;

import com.meteocal.business.controller.ScheduledController;
import com.meteocal.business.entities.User;
import com.meteocal.business.exceptions.InvalidInputException;
import com.meteocal.business.forecast.WeatherForecastService;
import com.meteocal.business.tests.EditEntityTest;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Andrea Bignoli
 */
@ManagedBean
@RequestScoped
public class TestWeatherForecastUpdaterBean {
    
    private static final Logger logger = Logger.getLogger(TestWeatherForecastUpdaterBean.class.getName());

    @EJB
    private WeatherForecastService weatherForecastUpdater;
    
    @EJB
    private ScheduledController scheduledController;

    public void testShortForecast() {
        weatherForecastUpdater.testShortRangeRequest();
    }
    
    public void testLongForecast() {
        weatherForecastUpdater.testLongRangeRequest();
    }
    
    public void testAskForecastsShort() {
        try {
            weatherForecastUpdater.askForecast(LocalDateTime.now().plusDays(1).minusHours(5), LocalDateTime.now().plusDays(2), "Monza", "ITALY");
        } catch (InvalidInputException ex) {
            Logger.getLogger(TestWeatherForecastUpdaterBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void testAskForecastsShortLong() {
        try {
            weatherForecastUpdater.askForecast(LocalDateTime.now().plusDays(1).minusHours(5), LocalDateTime.now().plusDays(8), "Monza", "ITALY");
        } catch (InvalidInputException ex) {
            Logger.getLogger(TestWeatherForecastUpdaterBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void testAskForecastsAllPossibilities() {
        try {
            // S = forecast start, E = forecast end
            // RS = forecast short range start (now), RM = forecast end short start long, RE = forecast long range end
            logger.log(Level.INFO, "S, E < RS");
            weatherForecastUpdater.askForecast(LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(4).minusHours(2), "Milano", "ITALY");
            logger.log(Level.INFO, "S < RS, RS < E < RM");
            weatherForecastUpdater.askForecast(LocalDateTime.now().minusHours(10), LocalDateTime.now().plusDays(weatherForecastUpdater.SHORT_RANGE_DAYS).minusHours(20), "Milano", "ITALY");            
            logger.log(Level.INFO, "S < RS, RM < E < RE");
            weatherForecastUpdater.askForecast(LocalDateTime.now().minusHours(10), LocalDateTime.now().plusDays(weatherForecastUpdater.LONG_RANGE_DAYS).minusHours(20), "Milano", "ITALY");   
            logger.log(Level.INFO, "S < RS, RE < E");
            weatherForecastUpdater.askForecast(LocalDateTime.now().minusHours(10), LocalDateTime.now().plusDays(weatherForecastUpdater.LONG_RANGE_DAYS + 4), "Milano", "ITALY");   
            logger.log(Level.INFO, "RS < S < RM, RS < E < RM");
            weatherForecastUpdater.askForecast(LocalDateTime.now().plusDays(weatherForecastUpdater.SHORT_RANGE_DAYS).minusHours(20), LocalDateTime.now().plusDays(weatherForecastUpdater.SHORT_RANGE_DAYS).minusHours(10), "Milano", "ITALY");   
            logger.log(Level.INFO, "RS < S < RM, RM < E < RE");
            weatherForecastUpdater.askForecast(LocalDateTime.now().plusDays(weatherForecastUpdater.SHORT_RANGE_DAYS).minusHours(20), LocalDateTime.now().plusDays(weatherForecastUpdater.LONG_RANGE_DAYS).minusHours(20), "Milano", "ITALY");
            logger.log(Level.INFO, "RS < S < RM, E > RE");
            weatherForecastUpdater.askForecast(LocalDateTime.now().plusDays(weatherForecastUpdater.SHORT_RANGE_DAYS).minusHours(20), LocalDateTime.now().plusDays(weatherForecastUpdater.LONG_RANGE_DAYS + 4), "Milano", "ITALY");            
            logger.log(Level.INFO, "RM < S < RE, RM < E < RE");
            weatherForecastUpdater.askForecast(LocalDateTime.now().plusDays(weatherForecastUpdater.LONG_RANGE_DAYS).minusHours(20), LocalDateTime.now().plusDays(weatherForecastUpdater.LONG_RANGE_DAYS).minusHours(10), "Milano", "ITALY");   
            logger.log(Level.INFO, "RM < S < RE, RE < E");
            weatherForecastUpdater.askForecast(LocalDateTime.now().plusDays(weatherForecastUpdater.LONG_RANGE_DAYS).minusHours(20), LocalDateTime.now().plusDays(weatherForecastUpdater.LONG_RANGE_DAYS + 4), "Milano", "ITALY");   
            logger.log(Level.INFO, "RE < S,E");
            weatherForecastUpdater.askForecast(LocalDateTime.now().plusDays(weatherForecastUpdater.LONG_RANGE_DAYS + 4), LocalDateTime.now().plusDays(weatherForecastUpdater.LONG_RANGE_DAYS + 6).plusHours(2), "Milano", "ITALY");   
            
        } catch (InvalidInputException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    
    public void testAskUpdateAllEvents() {
        scheduledController.updateEventsAndForecasts();
    }
    
}
