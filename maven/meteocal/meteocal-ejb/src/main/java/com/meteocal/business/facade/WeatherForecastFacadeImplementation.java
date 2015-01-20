/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.dao.WeatherForecastDAO;
import com.meteocal.business.entities.WeatherForecast;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author USUARIO
 */
@Stateless
public class WeatherForecastFacadeImplementation implements WeatherForecastFacade {

    @EJB
    WeatherForecastDAO weatherForecastDAO;
    
    @Override
    public void disable(WeatherForecast wf) {
        weatherForecastDAO.delete(wf);
    }

    @Override
    public List<WeatherForecast> askWeatherForecasts(LocalDateTime start, LocalDateTime end) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
