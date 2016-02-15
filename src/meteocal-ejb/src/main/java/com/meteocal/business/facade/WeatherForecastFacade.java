/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.entities.WeatherForecast;
import com.meteocal.business.entities.WeatherForecastBase;
import com.meteocal.business.exceptions.InvalidInputException;
import com.meteocal.business.exceptions.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Andrea Bignoli
 */
public interface WeatherForecastFacade {
    
    public void disable(WeatherForecast wf);
    
    public List<WeatherForecast> askWeatherForecasts(int eventID) throws InvalidInputException, NotFoundException;

    public void save(List<WeatherForecast> newForecasts);
    
    public void save(WeatherForecast newForecast);

    public List<WeatherForecastBase> askSuggestion(int eventId) throws NotFoundException, InvalidInputException;
    
}
