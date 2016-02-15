package com.meteocal.business.entities;

import com.meteocal.business.entities.Invitation;
import com.meteocal.business.entities.Notification;
import com.meteocal.business.entities.User;
import com.meteocal.business.entities.WeatherForecast;
import com.meteocal.business.entities.shared.EventStatus;
import java.time.LocalDateTime;
import java.util.EnumSet;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-25T23:45:24")
@StaticMetamodel(Event.class)
public class Event_ { 

    public static volatile SingularAttribute<Event, String> country;
    public static volatile SingularAttribute<Event, User> creator;
    public static volatile SingularAttribute<Event, String> address;
    public static volatile SingularAttribute<Event, String> city;
    public static volatile SingularAttribute<Event, LocalDateTime> start;
    public static volatile ListAttribute<Event, User> invited;
    public static volatile SingularAttribute<Event, String> description;
    public static volatile SingularAttribute<Event, Boolean> privateEvent;
    public static volatile SingularAttribute<Event, Boolean> suggestedChangeAvailable;
    public static volatile ListAttribute<Event, Invitation> invitations;
    public static volatile SingularAttribute<Event, String> name;
    public static volatile SingularAttribute<Event, EnumSet> adverseConditions;
    public static volatile SingularAttribute<Event, Boolean> indoor;
    public static volatile SingularAttribute<Event, Boolean> suggestedChangeProvided;
    public static volatile SingularAttribute<Event, LocalDateTime> end;
    public static volatile SingularAttribute<Event, Integer> id;
    public static volatile ListAttribute<Event, Notification> notifications;
    public static volatile SingularAttribute<Event, EventStatus> status;
    public static volatile ListAttribute<Event, User> participants;
    public static volatile ListAttribute<Event, WeatherForecast> weatherForecasts;

}