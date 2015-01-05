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
public class LoginBean {
    
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

        try {
            request.login(this.username, this.password);
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Login failed."));
            return "Index";
        }
        return "/protected/personal/HomeCalendarMonth";
    }
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().invalidate();
        return "/Index?faces-redirect=true";
    }
    
}
