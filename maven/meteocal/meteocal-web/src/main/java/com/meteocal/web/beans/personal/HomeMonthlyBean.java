/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.personal;

import com.meteocal.web.utility.SessionUtility;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.ScheduleEvent;

/**
 *
 * @author Leo
 */
@Named
@RequestScoped
public class HomeMonthlyBean implements Serializable{
    private ScheduleModel visibleEvents = new DefaultScheduleModel();
    private ScheduleEvent selectedEvent;
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    @Inject
    SessionUtility sessionUtility;
    
    @PostConstruct
    public void init(){
        visibleEvents = new DefaultScheduleModel();
        visibleEvents.addEvent(new DefaultScheduleEvent("Champions League Match", previousDay8Pm(), previousDay11Pm()));
        visibleEvents.addEvent(new DefaultScheduleEvent("Birthday Party", today1Pm(), nextDay9Am()));
        visibleEvents.addEvent(new DefaultScheduleEvent("Breakfast at Tiffanys", today1Pm(), nextDay9Am()));
        visibleEvents.addEvent(new DefaultScheduleEvent("Plant the new garden stuff", previousDay8Pm(), nextDay9Am()));
        request =  (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
     
    }
    
    public Date getRandomDate() {
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        date.add(Calendar.DATE, ((int) (Math.random()*30)) + 1);    //set random day of month
         
        return date.getTime();
    }
    
    public ScheduleModel getVisibleEvents(){
        return visibleEvents;
    }
    
    public void setVisibleEvents(ScheduleModel events){
        visibleEvents = events;
    }
  
    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }
     
    private Date previousDay8Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 8);
         
        return t.getTime();
    }
     
    private Date previousDay11Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 11);
         
        return t.getTime();
    }
     
    private Date today1Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 1);
         
        return t.getTime();
    }    
    private Date nextDay9Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 9);
         
        return t.getTime();
    }
     
}
