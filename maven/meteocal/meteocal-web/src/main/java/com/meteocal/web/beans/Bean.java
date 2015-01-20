package com.meteocal.web.beans;

import com.meteocal.business.entities.shared.WeatherCondition;
import com.meteocal.web.converters.WeatherConditionsConverter;
import com.meteocal.web.utility.SYSO_Testing;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@Named
@RequestScoped
public class Bean implements Serializable{
    private WeatherConditionsConverter conv;
    private List<WeatherCondition> weatherConditions,listChoiche;
    
    @PostConstruct
    public void init(){
        conv = new WeatherConditionsConverter();
        weatherConditions = new ArrayList<WeatherCondition>();
        listChoiche = new ArrayList<WeatherCondition>();
        weatherConditions.add(WeatherCondition.SUN);
        weatherConditions.add(WeatherCondition.SNOW);
        weatherConditions.add(WeatherCondition.RAIN);
        weatherConditions.add(WeatherCondition.CLOUDS);
    }
    
    public List<WeatherCondition> getWeatherConditions(){
        return weatherConditions;
    }
    
    public void setWeatherConditions(List<WeatherCondition> conditions){
        weatherConditions = conditions;
    }
    
    public List<WeatherCondition> getListChoiche(){
        return listChoiche;
    }
    
    public void setListChoiche(List<WeatherCondition> conditions){
        listChoiche = conditions;
    }
    
    public Converter getConv(){
        return conv;
    }
    
    public void mostra(){
        for(WeatherCondition date :listChoiche)
            SYSO_Testing.syso("bean. "+ date);
    }
}
