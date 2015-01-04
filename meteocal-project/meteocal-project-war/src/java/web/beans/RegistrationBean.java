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
    private String psw;
    private String confPsw;
    
    /**
     * Creates a new instance of LoginBean
     */
    public RegistrationBean() {
    }

    public void setName(String name){
        this.name = name;
        return;
    }
     
    public void setPsw(String password){
        this.psw = password;
        return;
    }
    
    public void setConfPsw(String confPsw){
        this.confPsw = confPsw;
        return;
    }
    
    public String getName(){
        return this.name;
    }
    public String getPsw(){
        return this.psw;
    }
    public String getConfPsw(){
        return this.confPsw;
    }
    
    public String add(){
        if( true ){
            return "Index";
        }
        else
            return "Error";
    }
}
