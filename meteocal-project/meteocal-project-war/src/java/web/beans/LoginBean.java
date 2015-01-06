/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author USUARIO
 */
@ManagedBean
@SessionScoped
//Is a Session Bean
public class LoginBean{
    
    private String username;
    private String password;
    
    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }

    public void setName(String name){
        this.username = name;
        return;
    }
     
    public void setPassword(String password){
        this.password = password;
        return;
    }
    
    public String getName(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
 
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        System.out.println("Login user: " + this.username + " psw: "+  this.password);
        try {
            System.out.println("1");
            
            request.login(this.username, this.password);
            
            System.out.println("2");
             } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Login failed."));
            System.out.println("user" + e.toString());
            return "/Error";
        }
        return "/protected/personal/HomeCalendarMonth";
    }
    
    public String logout() {
        System.out.println("logout was called from someone");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().invalidate();
        return "/Index";
    }
    
}
