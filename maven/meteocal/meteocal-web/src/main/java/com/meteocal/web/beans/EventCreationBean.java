/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.shared.WeatherCondition;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.web.converters.WeatherConditionsConverter;
import com.meteocal.web.utility.SYSO_Testing;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@Named
@RequestScoped
public class EventCreationBean implements Serializable {

    @EJB
    private EventFacade ef;

    private WeatherConditionsConverter conv;
    private List<WeatherCondition> weatherConditions;
    private EnumSet<WeatherCondition> listChoiche;
    private Event createdEvent;
    private boolean indoor, privateEvent;

    @PostConstruct
    public void init() {
        conv = new WeatherConditionsConverter();
        weatherConditions = new ArrayList<WeatherCondition>();
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

        SYSO_Testing.clean();
        SYSO_Testing.syso("address: " + createdEvent.getAddress() + " name: " + createdEvent.getName());
        SYSO_Testing.syso("city: " + createdEvent.getCity() + " country: " + createdEvent.getCountry());
        SYSO_Testing.syso("advCond: " + createdEvent.getAdverseConditions().toString() + " endTime: " + createdEvent.getEnd());

        ef.create(getCreatedEvent());

        return "/EventPage";
    }

    public Event getCreatedEvent() {
        return this.createdEvent;
    }

    public void setCreatedEvent(Event event) {
        this.createdEvent = event;
    }

    public List<WeatherCondition> getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(List<WeatherCondition> conditions) {
        weatherConditions = conditions;
    }

    public EnumSet<WeatherCondition> getListChoiche() {
        return listChoiche;
    }

    public void setListChoiche(EnumSet<WeatherCondition> conditions) {
        listChoiche = conditions;
    }

    public Converter getConv() {
        return conv;
    }

    public void mostra() {
        for (WeatherCondition date : listChoiche) {
            SYSO_Testing.syso("bean. " + date);
        }
    }
}
