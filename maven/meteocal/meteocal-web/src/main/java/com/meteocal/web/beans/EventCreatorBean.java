/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Leo
 */
@ManagedBean
@RequestScoped
public class EventCreatorBean {
    String user;

    public String addParticipant(){
        return "/protected/event/EventPageCreator.xhtml";
    }
}
