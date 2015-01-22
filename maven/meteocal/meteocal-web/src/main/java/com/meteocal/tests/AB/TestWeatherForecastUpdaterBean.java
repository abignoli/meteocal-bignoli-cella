/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.tests.AB;

import com.meteocal.business.entities.User;
import com.meteocal.business.forecast.WeatherForecastUpdater;
import com.meteocal.business.tests.EditEntityTest;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Andrea Bignoli
 */
@ManagedBean
@RequestScoped
public class TestWeatherForecastUpdaterBean {

    @EJB
    private WeatherForecastUpdater weatherForecastUpdater;

    public void test() {
        weatherForecastUpdater.testShortRangeRequest();
    }
}
