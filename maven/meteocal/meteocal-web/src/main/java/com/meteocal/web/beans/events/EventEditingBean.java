/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.events;

import com.meteocal.business.entities.Event;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.geography.GeographicRepository;
import com.meteocal.web.utility.SYSO_Testing;
import com.meteocal.web.utility.SessionUtility;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@ViewScoped
@Named
public class EventEditingBean implements Serializable {

    @EJB
    private GeographicRepository gp;

    @Inject
    private SessionUtility sessionUtility;

    @EJB
    private EventFacade ef;

    private List<String> cities, countries;
    private String address, city, country, name, description, selectedCountry;
    private Event event;

    private boolean indoor, privateEvent;

    @PostConstruct
    public void init() {
        countries = gp.getCountryNames();
        this.setEvent(ef.find(getID()));
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
        try {
            eventID = ef.create(getEvent()).getId();
        }
        catch (BusinessException e) {
            return "/Error";
        }
        sessionUtility.setParameter(eventID);
        return "/protected/event/EventPage";
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public boolean isIndoor() {
        return indoor;
    }

    public boolean isPrivateEvent() {
        return privateEvent;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIndoor(boolean indoor) {
        this.indoor = indoor;
    }

    public void setPrivateEvent(boolean privateEvent) {
        this.privateEvent = privateEvent;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
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
}
