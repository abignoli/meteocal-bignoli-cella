/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.utility;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
 
/**
 *
 * @author Leo
 */
public class HttpUtility {
      
    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }
    
    public static HttpServletRequest getRequest() {
       return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
    public static HttpServletResponse getResponse(){
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }
    
    public static String getQueryString(){
        return getRequest().getQueryString();
    }
    
    public static String getURI(){
        return getRequest().getRequestURI();
    }    
    
    public static String getURL(){
        return getRequest().getRequestURL().toString();
    }
    
    public static String getServerName(){
        return getRequest().getServerName();
    }
    
    public static String getRelativePath(){
        return getURI().replace("/meteocal-web","");
    }
    
    public int getErrorCode() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return  (int) session.getAttribute("errorCode");
    }
    public int getEventID() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return  (int) session.getAttribute("eventID");
    }
    public void setErrorCode(int err){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.setAttribute("errorCode",err);
    }
    public void setEventID(int event){    
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.setAttribute("eventID",event);
    }
}
