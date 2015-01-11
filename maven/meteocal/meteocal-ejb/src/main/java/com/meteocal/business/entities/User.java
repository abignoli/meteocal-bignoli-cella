/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.entities;

import com.meteocal.business.shared.data.Group;
import com.meteocal.business.shared.security.PasswordEncrypter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Andrea Bignoli
 */

@Entity
@Table(name = "USER")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
       
    @Column(unique=true)
    @NotNull(message = "Username cannot be empty")
    private String username;

    @NotNull(message = "Password cannot be empty")
    private String password;


        
//    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
//    message = "invalid email")
    @Column(unique = true)
    private String email;
        
    @NotNull(message = "Group name cannot be empty")
    private String groupName;
    
    @OneToMany(mappedBy="creator")
    private List<Event> createdEvents;    
    
    @ManyToMany
    @JoinTable(name="INVITATION",
            joinColumns = {@JoinColumn(name = "userID", 
                              referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "eventID", 
                              referencedColumnName = "id")})
    private List<Event> invitedTo;

    @OneToMany(mappedBy="user")
    private List<Invitation> invitations;
    
    @ManyToMany
    @JoinTable(name="PARTICIPATION",
            joinColumns = {@JoinColumn(name = "userID", 
                              referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "eventID", 
                              referencedColumnName = "id")})
    private List<Event> participatingTo;    
    
    @OneToMany(mappedBy="user")
    private List<NotificationView> notificationViews;
    
    @ManyToMany
    @JoinTable(name="NOTIFICATIONVIEW",
            joinColumns = {@JoinColumn(name = "userID", 
                              referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "notificationID", 
                              referencedColumnName = "id")})
    private List<Notification> notifications;   
        
    @NotNull
    @Column(columnDefinition="boolean default false")
    private boolean calendarVisible;
        
    public User() {
    }

    public int getId() {
        return this.id;
    }

    public List<Event> getParticipatingTo() {
        if(participatingTo == null)
            participatingTo = new ArrayList<Event>();
        
        return participatingTo;
    }

    public void setParticipatingTo(List<Event> participatingTo) {
        this.participatingTo = participatingTo;
    }
    
    public void partecipateTo(Event e) {
        // TODO check event is not already in list
        
        getParticipatingTo().add(e);
    }

    public void setId(int id) {
        this.id = id;
    }        
       
    public String getUsername() {
        return this.username;
    }
        
    public void setUsername() {
        this.username = username;
    }
    
        public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isCalendarVisible() {
        return calendarVisible;
    }

    public void setCalendarVisible(boolean calendarVisible) {
        this.calendarVisible = calendarVisible;
    }
    
    public List<Event> getInvitedTo() {
        return invitedTo;
    }

    public void setInvitedTo(List<Event> invitedTo) {
        this.invitedTo = invitedTo;
    }

    public boolean isValid() {
        // TODO implement logic
        return true;
    }

    public static String encryptPassword(String password) {
        return PasswordEncrypter.encrypt(password);
    }
}
