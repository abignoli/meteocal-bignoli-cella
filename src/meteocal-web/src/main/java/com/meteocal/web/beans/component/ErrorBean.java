/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.component;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Leonardo Cella
 */
@ManagedBean
@SessionScoped
public class ErrorBean implements Serializable{
    private String message="error";
    
    public String getMessage(){
        return message;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
}
