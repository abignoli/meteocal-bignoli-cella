/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@RequestScoped
@Named
public class EventEditingBean {
    private String address,city,country,name,description;

    private boolean indoor, privateEvent;
    
    public EventEditingBean(){
    }

    public String eventEditing(){
        return "/protected/event/EventPageCreator";
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
}
