/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.dao.EventDAO;
import com.meteocal.business.dao.WeatherForecastDAO;
import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.WeatherForecast;
import com.meteocal.business.entities.WeatherForecastBase;
import com.meteocal.business.entities.comparators.WeatherForecastBaseComparator;
import com.meteocal.business.exceptions.InvalidInputException;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.forecast.WeatherForecastService;
import com.meteocal.geography.GeographicRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    @EJB
    EventDAO eventDAO;

    @EJB
    GeographicRepository geographicRepository;

    @EJB
    WeatherForecastService weatherForecastService;

    public void disable(WeatherForecast wf) {
        weatherForecastDAO.delete(wf);
    }

    private List<WeatherForecastBase> askWeatherForecasts(LocalDateTime start, LocalDateTime end, String city, String country) throws InvalidInputException {
        return weatherForecastService.askForecast(start, end, city, country);
    }

    @Override
    public List<WeatherForecast> askWeatherForecasts(int eventID) throws NotFoundException, InvalidInputException {
        Event e = eventDAO.retrieve(eventID);

        eventDAO.refresh(e);

        return assign(e, askWeatherForecasts(e.getStart(), e.getEnd(), e.getCity(), e.getCountry()));
    }

    private List<WeatherForecast> assign(Event e, List<WeatherForecastBase> forecastsBase) {
        if (e == null) {
            throw new NullPointerException();
        }

        List<WeatherForecast> forecasts = new ArrayList<WeatherForecast>();

        if (forecastsBase != null) {
            for (WeatherForecastBase wfb : forecastsBase) {
                forecasts.add(new WeatherForecast(wfb, e));
            }
        }
        
        Collections.sort(forecasts, new WeatherForecastBaseComparator());
        
        return forecasts;
    }

    @Override
    public void save(List<WeatherForecast> newForecasts) {
        for(WeatherForecast wf: newForecasts)
            save(wf);
    }

    @Override
    public void save(WeatherForecast newForecast) {
        weatherForecastDAO.save(newForecast);
    }

    @Override
    public List<WeatherForecastBase> askSuggestion(int eventId) throws NotFoundException, InvalidInputException {
        Event e = eventDAO.retrieve(eventId);
        
        return weatherForecastService.askClosestMatch(e.getStart(), e.getEnd(), e.getCity(), e.getCountry(), e.getAdverseConditions());
    }

}
