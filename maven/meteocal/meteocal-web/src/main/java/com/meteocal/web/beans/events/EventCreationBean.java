/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.events;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.shared.WeatherCondition;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.InvalidInputException;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.business.forecast.WeatherForecastService;
import com.meteocal.geography.GeographicRepository;
import com.meteocal.web.beans.component.ErrorBean;
import com.meteocal.web.converters.WeatherConditionsConverter;
import com.meteocal.web.utility.SYSO_Testing;
import com.meteocal.web.utility.SessionUtility;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.convert.Converter;
import javax.inject.Inject;
/**
 *
 * @author Leo
 */
@ManagedBean
@ViewScoped
public class EventCreationBean implements Serializable {

    @EJB
    private EventFacade ef;

    @EJB
    private GeographicRepository gp;

    @Inject
    private ErrorBean error;
    
    @EJB
    private WeatherForecastService weatherForecastUpdater;

    @Inject
    private SessionUtility sessionUtility;

    private WeatherConditionsConverter weatherConv;
    private EnumSet<WeatherCondition> listChoiche, weatherConditions;
    private Event createdEvent;
    private List<String> cities, countries;
    private String selectedCountry,participants;
    private List<WeatherCondition> weatherForecast;

    @PostConstruct
    public void init() {
        countries = gp.getCountryNames();
        weatherConv = new WeatherConditionsConverter();
        weatherConditions = EnumSet.noneOf(WeatherCondition.class);
        listChoiche = EnumSet.noneOf(WeatherCondition.class);
        weatherConditions.add(WeatherCondition.SUN);
        weatherConditions.add(WeatherCondition.SNOW);
        weatherConditions.add(WeatherCondition.RAIN);
        weatherConditions.add(WeatherCondition.CLOUDS);
        this.setEvent(new Event());
    }

    private void setEvent(Event event) {
        this.createdEvent = event;
    }

    public String create() {
        int eventID;
        SYSO_Testing.syso("in create()");
        SYSO_Testing.syso("address: " + createdEvent.getAddress() + " name: " + createdEvent.getName());
        SYSO_Testing.syso("city: " + createdEvent.getCity() + " country: " + createdEvent.getCountry());
        SYSO_Testing.syso("start: " + createdEvent.getStart().toString() + "end" + createdEvent.getEnd().toString());
        SYSO_Testing.syso("advCond: " + createdEvent.getAdverseConditions().size());
        createdEvent.setCountry(selectedCountry);
        try {
            eventID = ef.create(getCreatedEvent()).getId();
            //ef.invite(participants);
        }
        catch (BusinessException e) {
            error.setMessage("An error occurs: " + e.getMessage());
            return "/Error";
        }

        sessionUtility.setParameter(eventID);
        return "/protected/event/EventPage";
    }

    public Event getCreatedEvent() {
        return this.createdEvent;
    }

    public void setCreatedEvent(Event event) {
        this.createdEvent = event;
    }

    public EnumSet<WeatherCondition> getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(EnumSet<WeatherCondition> conditions) {
        weatherConditions = conditions;
    }

    public EnumSet<WeatherCondition> getListChoiche() {
        return listChoiche;
    }

    public void setListChoiche(EnumSet<WeatherCondition> conditions) {
        listChoiche = conditions;
    }

    public Converter getWeatherConv() {
        return weatherConv;
    }

    public void updateCities() {
        cities = gp.getCityNames(selectedCountry);
    }

    public void mostra() {
        for (WeatherCondition date : listChoiche) {
            SYSO_Testing.syso("bean. " + date);
        }
    }

    public void metodoDiProva() {
        SYSO_Testing.syso("prova listener");
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public void setParticipants(String participant){
        this.participants = participant;
    }
    
    public String getParticipants(){
        return participants;
    }
    
    public void setSelectedCountry(String selectedCountry){
        this.selectedCountry = selectedCountry;
    }
    
    public String getSelectedCountry(){
        return selectedCountry;
    }

    public List<WeatherCondition> getWeatherForecast() {
        return weatherForecast;
    }

    public void setWeatherForecast(List<WeatherCondition> weatherForecastConditions) {
        this.weatherForecast = weatherForecastConditions;
    }
    
    public void loadWeatherConditions(){
        try {
            weatherForecastUpdater.askForecast(createdEvent.getStart(), createdEvent.getEnd(), createdEvent.getCity(), createdEvent.getCountry() );
            setWeatherForecast(weatherForecast);
        }
        catch (InvalidInputException ex) {
            Logger.getLogger(EventCreationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
