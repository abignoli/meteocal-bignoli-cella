/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.entities;

import business.shared.security.PasswordEncrypter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author USUARIO
 */

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
       
    @Column(unique=true)
    private String username;

    @NotNull(message = "Password cannot be empty")
    private String password;
        
    @Id
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
    message = "invalid email")
    @NotNull(message = "Email cannot be empty")
    @Column(unique = true)
    private String email;
        
    @NotNull(message = "Group name cannot be empty")
    private String groupName;
        
    //TODO default value
    private boolean calendarVisible;
        
    public User() {
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

    public boolean isValid() {
        // TODO implement logic
        return true;
    }

    public void encryptPassword() {
        password = PasswordEncrypter.encrypt(password);
    }

    
}
