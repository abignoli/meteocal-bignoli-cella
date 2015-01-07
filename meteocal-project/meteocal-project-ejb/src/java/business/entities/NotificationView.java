/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.entities;

import business.entities.keys.NotificationViewID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Andrea Bignoli
 */

@Entity
@IdClass(NotificationViewID.class)
@Table(name = "NOTIFICATIONVIEW")
public class NotificationView {
    
    @Id
    private int userID;
    
    @Id
    private int notificationID;
    
    @NotNull
    @Column(columnDefinition="boolean default false")
    private boolean seen;

    @ManyToOne(fetch=FetchType.LAZY)
    @PrimaryKeyJoinColumn(name="USERID", referencedColumnName = "ID")
    private User user;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @PrimaryKeyJoinColumn(name="NOTIFICATIONID", referencedColumnName = "ID")
    private Notification notification;
    
    
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int eventID) {
        this.notificationID = eventID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
    
    
}
