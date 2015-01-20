/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.entities.WeatherForecast;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public interface WeatherForecastFacade {

    public void disable(WeatherForecast wf);

    public List<WeatherForecast> askWeatherForecasts(LocalDateTime start, LocalDateTime end);
    
}
