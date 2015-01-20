/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.converters;

import com.meteocal.web.utility.SYSO_Testing;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Leo
 */
public class TimeConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        SYSO_Testing.syso("getAsObj-Time. " + value);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(value, formatter);
        return dateTime;
        
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String ret = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //LocalDateTime dateTime = (LocalDateTime) value;
        // ret = dateTime.format(formatter); // "1986-04-08 12:30"
        return ret;
    }
    
}
