package com.meteocal.business.entities;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-25T23:45:24")
@StaticMetamodel(Invitation.class)
public class Invitation_ { 

    public static volatile SingularAttribute<Invitation, Integer> eventID;
    public static volatile SingularAttribute<Invitation, Boolean> declined;
    public static volatile SingularAttribute<Invitation, Event> event;
    public static volatile SingularAttribute<Invitation, Integer> userID;
    public static volatile SingularAttribute<Invitation, User> user;
    public static volatile SingularAttribute<Invitation, Boolean> seen;

}