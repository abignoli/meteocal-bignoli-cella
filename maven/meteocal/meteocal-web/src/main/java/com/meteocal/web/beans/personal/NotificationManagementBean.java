/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.personal;

import com.meteocal.business.entities.Notification;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Leo
 */
@RequestScoped
@Named
public class NotificationManagementBean {

    List<Notification> notifications;
    
    public List<Notification> getNotifications(){
        return notifications;
    }
    
    public void setNotifications(List<Notification> newNotifications){
        notifications = newNotifications;
    }
}
