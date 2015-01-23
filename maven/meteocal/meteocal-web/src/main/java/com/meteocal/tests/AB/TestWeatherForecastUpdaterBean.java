/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.tests.AB;

import com.meteocal.business.entities.User;
import com.meteocal.business.exceptions.InvalidInputException;
import com.meteocal.business.forecast.WeatherForecastUpdater;
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

    @EJB
    private WeatherForecastUpdater weatherForecastUpdater;

    public void testShortForecast() {
        weatherForecastUpdater.testShortRangeRequest();
    }
    
    public void testLongForecast() {
        weatherForecastUpdater.testLongRangeRequest();
    }
    
    public void testAskForecastsShort() {
        try {
            weatherForecastUpdater.askForecast(LocalDateTime.now().plusDays(1).minusHours(5), LocalDateTime.now().plusDays(2), "Monza", "IT");
        } catch (InvalidInputException ex) {
            Logger.getLogger(TestWeatherForecastUpdaterBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void testAskForecastsShortLong() {
        try {
            weatherForecastUpdater.askForecast(LocalDateTime.now().plusDays(1).minusHours(5), LocalDateTime.now().plusDays(8), "Monza", "IT");
        } catch (InvalidInputException ex) {
            Logger.getLogger(TestWeatherForecastUpdaterBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
