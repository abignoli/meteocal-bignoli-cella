/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.entities;

import com.meteocal.business.entities.shared.EventStatus;
import com.meteocal.business.entities.shared.WeatherCondition;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Andrea Bignoli
 */
@Entity
@Table(name = "EVENT")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    @NotNull(message = "Event name cannot be empty")
    private String name;

    @NotNull(message = "Event description cannot be empty")
    private String description;
    
//    TODO remove comment
//    @NotNull
    private LocalDateTime start;

//    TODO remove comment
//    @NotNull
    private LocalDateTime end;
    
//    TODO remove comment
//    @NotNull    
    private EventStatus status = EventStatus.PLANNED;
    
    private String country;

    private String city;

    private String address;

    private boolean indoor;

    // TODO Bad Weather Conditions set
    
    private boolean privateEvent;

    private LocalDateTime suggestedChangeStart;
    
    private LocalDateTime suggestedChangeEnd;
    
    
    
    
    
    @ElementCollection(targetClass = WeatherCondition.class) 
    @CollectionTable(name = "ADVERSE_CONDITIONS",
        joinColumns = @JoinColumn(name = "eventID",
                        referencedColumnName = "id"))
    @Column(name = "adverseCondition")
    @Enumerated(EnumType.STRING)
    EnumSet<WeatherCondition> adverseConditions;

    
    
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATORID", referencedColumnName = "ID")
    private User creator;

    @OneToMany(mappedBy = "event")
    private List<Invitation> invitations;

    @OneToMany(mappedBy = "event")
    private List<Notification> notifications;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "PARTICIPATION",
            joinColumns = {
                @JoinColumn(name = "eventID",
                        referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "userID",
                        referencedColumnName = "id")})
    private List<User> participants;

    @ManyToMany
    @JoinTable(name = "INVITATION",
            joinColumns = {
                @JoinColumn(name = "eventID",
                        referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "userID",
                        referencedColumnName = "id")})
    private List<User> invited;

    @OneToMany(mappedBy = "event")
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
        if (invitations == null) {
            invitations = new ArrayList<Invitation>();
        }

        return invitations;
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }

    public List<User> getInvited() {
        if (invited == null) {
            invited = new ArrayList<User>();
        }

        return invited;
    }

    public void setInvited(List<User> invited) {
        this.invited = invited;
    }

    public void addInvited(User u) {
        List<User> invited = this.getInvited();

        if (!isInvited(u)) {
            invited.add(u);
            u.getParticipatingTo().add(this);
        }
    }

    public void removeInvited(User u) {
        List<User> invited = this.getInvited();
        
        User invitedUser = findInvited(u);

        if (invitedUser != null) {
            invited.remove(invitedUser);
            invitedUser.removeInvitedToFromList(this);
            u.removeInvitedToFromList(this);
        }
    }
    
    public boolean isInvited(User u) {
        for (User invited : getInvited()) {
            if (invited.getId() == u.getId()) {
                return true;
            }
        }

        return false;
    }
    
    public User findInvited(User u) {
        for (User invited : getInvited()) {
            if (invited.getId() == u.getId()) {
                return invited;
            }
        }

        return null;
    }

    public List<User> getParticipants() {
        if (participants == null) {
            participants = new ArrayList<User>();
        }

        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public void addParticipant(User u) {
        List<User> participants = this.getParticipants();

        if (!isParticipant(u)) {
            participants.add(u);
            u.getParticipatingTo().add(this);
        }
    }

    public void removeParticipant(User u) {
        List<User> participants = this.getParticipants();
        User participant = findParticipant(u);

        if (participant != null) {
            participants.remove(participant);
            participant.removeParticipatingToFromList(this);
            u.removeParticipatingToFromList(this);
        }
    }

    public boolean isParticipant(User u) {
        for (User participant : getParticipants()) {
            if (participant.getId() == u.getId()) {
                return true;
            }
        }

        return false;
    }

    public User findParticipant(User u) {
        for (User participant : getParticipants()) {
            if (participant.getId() == u.getId()) {
                return participant;
            }
        }

        return null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isIndoor() {
        return indoor;
    }

    public void setIndoor(boolean indoor) {
        this.indoor = indoor;
    }

    public boolean isPrivateEvent() {
        return privateEvent;
    }

    public void setPrivateEvent(boolean privateEvent) {
        this.privateEvent = privateEvent;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<WeatherForecast> getWeatherForecasts() {
        return weatherForecasts;
    }

    public void setWeatherForecasts(List<WeatherForecast> weatherForecasts) {
        this.weatherForecasts = weatherForecasts;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public LocalDateTime getSuggestedChangeStart() {
        return suggestedChangeStart;
    }

    public void setSuggestedChangeStart(LocalDateTime suggestedChangeStart) {
        this.suggestedChangeStart = suggestedChangeStart;
    }

    public LocalDateTime getSuggestedChangeEnd() {
        return suggestedChangeEnd;
    }

    public void setSuggestedChangeEnd(LocalDateTime suggestedChangeEnd) {
        this.suggestedChangeEnd = suggestedChangeEnd;
    }

    public EnumSet<WeatherCondition> getAdverseConditions() {
        return adverseConditions;
    }

    public void setAdverseConditions(EnumSet<WeatherCondition> adverseConditions) {
        this.adverseConditions = adverseConditions;
    }
    
    
}
