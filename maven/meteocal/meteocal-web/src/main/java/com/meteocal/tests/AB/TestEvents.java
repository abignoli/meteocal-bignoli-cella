/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.tests.AB;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import com.meteocal.business.tests.EditEntityTest;
import com.meteocal.business.tests.EventTests;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author ate
 */
@ManagedBean
@RequestScoped
public class TestEvents {
    @EJB
    EventTests eventTests;

    public void test() {
        eventTests.findAll();
    }
    
    public void testCreateAndForecast() {
        User creator = eventTests.testCreateAndForecast1();
        Event event = eventTests.testCreateAndForecast2(creator.getId());
        eventTests.testCreateAndForecast3(event.getId());
    }
    
    public void testCreateAndAskSuggestion() {
        User creator = eventTests.testCreateAndAskSuggestion1();
        Event event = eventTests.testCreateAndAskSuggestion2(creator.getId());
        eventTests.testCreateAndAskSuggestion3(event.getId());
    }
}
