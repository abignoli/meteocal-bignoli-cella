/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.entities.keys;

import java.io.Serializable;

/**
 *
 * @author Andrea Bignoli
 */
public class NotificationViewID implements Serializable {
    private int userID;
    private int notificationID;

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
