/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.beans;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author USUARIO
 */
@ManagedBean
@SessionScoped
//Is a Session Bean
public class RegistrationBean {
    
    private String name;
    private String password;
    private String passwordConfirmation;
    
    /**
     * Creates a new instance of LoginBean
     */
    public RegistrationBean() {
    }

    public void setName(String name){
        this.name = name;
        System.out.println(this.name);
    }
     
    public void setPassword(String password){
        this.password = password;
    }
    
    public void setPasswordConfirmation(String passwordConf){
        this.passwordConfirmation = passwordConf;
    }
    
    public String getName(){
        return this.name;
    }
    public String getPassword(){
        return this.password;
    }
    public String getPasswordConfirmation(){
        return this.passwordConfirmation;
    }
    
    public String add(){
        if( true ){
            return "Index";
        }
        else
            return "Error";
    }
}
