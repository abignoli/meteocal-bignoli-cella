package com.meteocal.business.entities;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import com.meteocal.business.entities.shared.NotificationType;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-25T23:45:24")
@StaticMetamodel(Notification.class)
public class Notification_ { 

    public static volatile ListAttribute<Notification, User> notificatedUsers;
    public static volatile SingularAttribute<Notification, Boolean> goodWeather;
    public static volatile SingularAttribute<Notification, Integer> id;
    public static volatile SingularAttribute<Notification, Event> event;
    public static volatile SingularAttribute<Notification, NotificationType> type;
    public static volatile SingularAttribute<Notification, LocalDateTime> generationDateTime;

}