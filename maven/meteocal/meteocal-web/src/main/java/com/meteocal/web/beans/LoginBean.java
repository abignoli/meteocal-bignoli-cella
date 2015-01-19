/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans;

import com.meteocal.web.utility.SessionUtility;
import java.io.IOException;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author USUARIO
 */
@ManagedBean
@RequestScoped
public class LoginBean {

    private String username;
    private String password;
   
    @Inject
    SessionUtility sessionUtility;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }

    public void setName(String name) {
        this.username = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        System.out.println("Try to login user: " + this.username + " with psw: " + this.password + " !");
        try {
            request.login(this.username, this.password);
        }
        catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Login failed."));
            System.out.println("user" + e.toString());
            return "/Error";
        }
        System.out.println("Login successful");
        sessionUtility.addUser(username);

        return "/protected/personal/HomeCalendarMonth";
    }

    public void logout() {
        System.out.println("starting logout!");
        
        String contextPath;
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        sessionUtility.sessionLogout();
        request.getSession().invalidate();
        try {
            contextPath = request.getContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/Index.xhtml");
        }
        catch (IOException ex) {
        }
    }

}
