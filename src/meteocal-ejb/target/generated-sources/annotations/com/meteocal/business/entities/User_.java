package com.meteocal.business.entities;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.Invitation;
import com.meteocal.business.entities.Notification;
import com.meteocal.business.entities.NotificationView;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-25T23:45:24")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, Boolean> calendarVisible;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, String> groupName;
    public static volatile ListAttribute<User, Event> participatingTo;
    public static volatile ListAttribute<User, Invitation> invitations;
    public static volatile ListAttribute<User, Event> invitedTo;
    public static volatile ListAttribute<User, NotificationView> notificationViews;
    public static volatile ListAttribute<User, Event> createdEvents;
    public static volatile SingularAttribute<User, Integer> id;
    public static volatile SingularAttribute<User, String> email;
    public static volatile ListAttribute<User, Notification> notifications;
    public static volatile SingularAttribute<User, String> username;

}