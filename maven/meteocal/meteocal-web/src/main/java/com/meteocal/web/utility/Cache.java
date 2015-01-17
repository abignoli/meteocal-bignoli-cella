/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.utility;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Leo
 */
@ManagedBean
@SessionScoped
public class Cache implements Serializable {

    private int errorType, eventID;
    private boolean error;

    public int getErrorType() {
        return this.errorType;
    }

    public int getEventID() {
        return this.eventID;
    }

    public boolean getError() {
        return this.error;
    }
    
    public void setErrorType(int err){
        this.errorType = err;
    }
    public void setEventID(int event){
        this.eventID = event;
    }
    
    public void setErrorType(boolean err){
        this.error = err;
    }
}
