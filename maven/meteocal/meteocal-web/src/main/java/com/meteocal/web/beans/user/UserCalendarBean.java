/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.user;

import com.meteocal.business.entities.Event;
import com.meteocal.business.facade.UserFacade;
import com.meteocal.business.security.UserManager;
import com.meteocal.web.converters.ConverterLocalDateTimeAndDate;
import com.meteocal.web.exceptions.NotValidParameter;
import com.meteocal.web.utility.SessionUtility;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Leo
 */
public class UserCalendarBean {
//    private ScheduleModel visibleEvents;
//    private List<Event> userEvents;
//    private final HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//
//    @Inject
//    SessionUtility sessionUtility;
//
//    @EJB
//    UserManager um;
//    
//    @EJB
//    UserFacade uf;
//
//    @PostConstruct
//    public void init() {
//        Date d1, d2;
//        DefaultScheduleEvent tmpEvent;
//
////        String username = GET USERNAME FROM REQUEST
//
//        userEvents = um.getEventsVisibilityMasked(um.getEventsVisibilityMasked(uf.findByUsername(username)));
//
//        visibleEvents = new DefaultScheduleModel();
//        for (Event myEvent : userEvents) {
//            d1 = ConverterLocalDateTimeAndDate.toDate(myEvent.getStart());
//            d2 = ConverterLocalDateTimeAndDate.toDate(myEvent.getEnd());
//            tmpEvent = new DefaultScheduleEvent(myEvent.getName(), d1, d2, myEvent.getId());
//            visibleEvents.addEvent(tmpEvent);
//        }
//
//    }
//    }
//
//    public ScheduleModel getVisibleEvents() {
//        return visibleEvents;
//    }
//
//    public void setVisibleEvents(ScheduleModel events) {
//        visibleEvents = events;
//    }
//
//    private Calendar today() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
//
//        return calendar;
//    }
//
//    private Date previousDay8Pm() {
//        Calendar t = (Calendar) today().clone();
//        t.set(Calendar.AM_PM, Calendar.PM);
//        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
//        t.set(Calendar.HOUR, 8);
//
//        return t.getTime();
//    }
//
//    private Date previousDay11Pm() {
//        Calendar t = (Calendar) today().clone();
//        t.set(Calendar.AM_PM, Calendar.PM);
//        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
//        t.set(Calendar.HOUR, 11);
//
//        return t.getTime();
//    }
}
