/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.utility;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Leo
 */
@Named
@SessionScoped
public class SessionUtility implements Serializable {

    private String loggedUserName,requiredUser;
    private int errorCode, parameterID;
    private boolean error,parameter,isUser;

    @PostConstruct
    public void init() {
        error = false;
        isUser = false;
    }

    public void addUser(String username) {
        loggedUserName = username;
        SYSO_Testing.syso("SessionUtility_addUser: " + loggedUserName + " added in cache");
    }

    public String getLoggedUser() {
        return loggedUserName;
    }

    public void sessionLogout() {
        SYSO_Testing.syso("SessionUtility_logout");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        loggedUserName = null;
        error = false;
        request.getSession().invalidate();

    }

    public void setError(int value) {
        errorCode = value;
        error = true;
    }

    public boolean isAnError() {
        return error;
    }

    public int getError() {
        return errorCode;
    }

    public void setParameter(int value) {
        parameterID = value;
        parameter = true;
    }

    public boolean isAParameter() {
        return parameter;
    }

    public int getParameter() {
        return parameterID;
    }
    
    public int getParameterAsClient() {
        parameter = false;
        return parameterID;
    }

    public void showedError() {
        error = false;
    }

    public boolean isThereAnActiveSession() {
        return this.loggedUserName != null;
    }
    
    public void setUser(String username) { 
        requiredUser = username;
        isUser = true;
    }
    
    public boolean isAUser(){
        return isUser;
    }
    
    public String getUser(){
        return requiredUser;
    }
    
    public String getUserAsClient(){
        isUser = false;
        return requiredUser;
    }
}

