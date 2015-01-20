/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.converters;

import com.meteocal.business.entities.shared.WeatherCondition;
import com.meteocal.web.utility.SYSO_Testing;
import java.util.EnumSet;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Leo
 */
@FacesConverter(value="convertitore")
public class WeatherConditionsConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        WeatherCondition ret = null;
        switch(value.toUpperCase()){
            case "CLOUDS":
                ret = WeatherCondition.CLOUDS;
                break;
            case "RAIN":
                ret = WeatherCondition.RAIN;
                break;
            case "SNOW":
                ret = WeatherCondition.SNOW;
                break;
            case "SUN":
                ret = WeatherCondition.SUN;
                break;
        }
        SYSO_Testing.syso("getAsObj." + ret.toString());
        return ret;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        SYSO_Testing.syso("getAsString. " + value.toString());
        return value.toString();
    }
    
}
