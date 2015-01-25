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
import com.meteocal.web.converters.WeatherConditionsConverter;
import com.meteocal.web.utility.SessionUtility;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

/**
 *
 * @author Leo
 */
@ViewScoped
@ManagedBean
public class EventEditingBean implements Serializable {

    @EJB
    private GeographicRepository gp;

    @EJB
    private WeatherForecastService weatherForecastUpdater;

    @Inject
    private SessionUtility sessionUtility;

    @EJB
    private EventFacade ef;

    private List<String> cities, countries;
    private String address, city, country, name, description, selectedCountry;
    private Event event;
    private WeatherConditionsConverter weatherConv;
    private EnumSet<WeatherCondition> listChoiche, weatherConditions;
    private List<WeatherCondition> weatherForecast;
    private boolean indoor, privateEvent;

    @PostConstruct
    public void init() {
        countries = gp.getCountryNames();
        this.setEvent(ef.find(getID()));
        weatherConv = new WeatherConditionsConverter();
        weatherConditions = EnumSet.noneOf(WeatherCondition.class);
        listChoiche = EnumSet.noneOf(WeatherCondition.class);
        weatherConditions.add(WeatherCondition.SUN);
        weatherConditions.add(WeatherCondition.SNOW);
        weatherConditions.add(WeatherCondition.RAIN);
        weatherConditions.add(WeatherCondition.CLOUDS);
    }

    public EventEditingBean() {
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event1) {
        event = event1;
    }

    public String eventEditing() {
        int eventID;
        event.setCountry(selectedCountry);
        if( isThereAnError() ){
            return "";
        }
            
        try {
            ef.updateData(getEvent());
        }
        catch (BusinessException ex) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "Error");
        }

        eventID = sessionUtility.getParameterAsClient();
        sessionUtility.setParameter(eventID);
        return "/protected/event/EventPage?faces-redirect=true";
    }

    public String getCity() {
        return city;
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

    private int getID() {
        return sessionUtility.getParameterAsClient();
    }

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public void updateCities() {
        cities = gp.getCityNames(selectedCountry);
    }

    public String getSelectedCountry() {
        return selectedCountry;
    }

    public List<WeatherCondition> getWeatherForecast() {
        return weatherForecast;
    }

    public void setWeatherForecast(List<WeatherCondition> weatherForecastConditions) {
        this.weatherForecast = weatherForecastConditions;
    }

    public void loadWeatherConditions() {
        try {
            weatherForecastUpdater.askForecast(event.getStart(), event.getEnd(), event.getCity(), event.getCountry());
            setWeatherForecast(weatherForecast);
        }
        catch (InvalidInputException ex) {
            Logger.getLogger(EventCreationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public boolean isThereAnError() {
        LocalDateTime start, end,now;
        start = event.getStart();
        end = event.getEnd();
        now = LocalDateTime.now();
        
        return start.isAfter(now.plusMinutes(15)) && start.isBefore(end);
    }
}
