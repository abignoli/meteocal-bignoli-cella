/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.entities;

import com.meteocal.business.entities.shared.TableDictionary;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Andrea Bignoli
 */
@Entity
@Table(name = TableDictionary.TABLE_WEATHER_FORECAST)
public class WeatherForecast extends WeatherForecastBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="EVENTID", referencedColumnName = "ID")
    private Event event;

    public WeatherForecast() {
    }

    public WeatherForecast(WeatherForecastBase wfb, Event e) {
        if(wfb == null || e == null)
            throw new NullPointerException();

        weatherCondition = wfb.getWeatherCondition();
        forecastStart = wfb.getForecastStart();
        forecastEnd = wfb.getForecastEnd();
        
        event = e;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
