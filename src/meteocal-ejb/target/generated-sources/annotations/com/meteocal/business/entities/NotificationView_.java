package com.meteocal.business.entities;

import com.meteocal.business.entities.Notification;
import com.meteocal.business.entities.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-25T23:45:24")
@StaticMetamodel(NotificationView.class)
public class NotificationView_ { 

    public static volatile SingularAttribute<NotificationView, Notification> notification;
    public static volatile SingularAttribute<NotificationView, Integer> notificationID;
    public static volatile SingularAttribute<NotificationView, Integer> userID;
    public static volatile SingularAttribute<NotificationView, User> user;
    public static volatile SingularAttribute<NotificationView, Boolean> seen;

}