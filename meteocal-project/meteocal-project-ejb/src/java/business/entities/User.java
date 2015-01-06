/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.entities;

import business.shared.data.Group;
import business.shared.security.PasswordEncrypter;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author USUARIO
 */

@Entity
@Table(name = "USER")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
       
    @Column(unique=true)
    private String username;

    @NotNull(message = "Password cannot be empty")
    private String password;

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
        
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
    message = "invalid email")
    @Column(unique = true)
    private String email;
        
    @NotNull(message = "Group name cannot be empty")
    private String groupName;
    
//    @ManyToMany
//    @JoinTable(name="INVITATION",
//            joinColumns = {@JoinColumn(table = "INVITATION", name = "userID", 
//                              referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(table = "EVENT", name = "eventID", 
//                              referencedColumnName = "id")})
//    private List<Event> invitedTo;

//    @OneToMany(mappedBy="userID")
//    private List<Invitation> invitations;
        
    //TODO default value
    private boolean calendarVisible;
        
    public User() {
        groupName = Group.USER.getName();
    }

    public int getId() {
        return this.id;
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
    
//    public List<Event> getInvitedTo() {
//        return invitedTo;
//    }
//
//    public void setInvitedTo(List<Event> invitedTo) {
//        this.invitedTo = invitedTo;
//    }

    public boolean isValid() {
        // TODO implement logic
        return true;
    }

    public void encryptPassword() {
        password = PasswordEncrypter.encrypt(password);
    }

    
}
