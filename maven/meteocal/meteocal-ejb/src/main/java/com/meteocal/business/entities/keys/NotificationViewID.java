/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.entities.keys;

import java.io.Serializable;

/**
 *
 * @author Andrea Bignoli
 */
public class NotificationViewID implements Serializable {
    private int userID;
    private int notificationID;

    public NotificationViewID() {
    }

    public NotificationViewID(int userID, int notificationID) {
        this.userID = userID;
        this.notificationID = notificationID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.userID;
        hash = 59 * hash + this.notificationID;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NotificationViewID other = (NotificationViewID) obj;
        if (this.userID != other.userID) {
            return false;
        }
        if (this.notificationID != other.notificationID) {
            return false;
        }
        return true;
    }
}
