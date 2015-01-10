/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Andrea Bignoli
 */

@Entity
@Table(name="EVENT")
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(unique=true)
    @NotNull(message = "Event name cannot be empty")
    private String name;
    
    @NotNull(message = "Event description cannot be empty")
    private String description;
    
    // TODO add start and end datetime
 
    private String country;
    
    private String city;
    
    private String address;
    
    private boolean indoor;
    
    // TODO Bad Weather Conditions set
    
    private boolean privateEvent;
    
    // TODO Enum with Event State
    
    // TODO Suggested schedule change datetime
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CREATORID", referencedColumnName = "ID")
    private User creator;
    
    @OneToMany(mappedBy="event")
    private List<Invitation> invitations;
    
    @OneToMany(mappedBy="event")
    private List<Notification> notifications;
    
    @ManyToMany
    @JoinTable(name="PARTICIPATION",
            joinColumns = {@JoinColumn(name = "eventID", 
                              referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "userID", 
                              referencedColumnName = "id")})
    private List<User> participants;  
    
    @ManyToMany
    @JoinTable(name="INVITATION",
            joinColumns = {@JoinColumn(name = "eventID", 
                              referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "userID", 
                              referencedColumnName = "id")})
    private List<User> invited; 
    
    @OneToMany(mappedBy="event")
    private List<WeatherForecast> weatherForecasts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Invitation> getInvitations() {
        if(invitations == null)
            invitations = new ArrayList<Invitation>();
        
        return invitations;
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }

    public List<User> getInvited() {
        if(invited == null)
            invited = new ArrayList<User>();
        
        return invited;
    }

    public void setInvited(List<User> invited) {
        this.invited = invited;
    }
    
    public void invite(User u) {
        // TODO check event is not already in list
        
        getInvited().add(u);
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }
    
    
}
