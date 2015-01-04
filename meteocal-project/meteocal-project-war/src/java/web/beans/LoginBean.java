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
public class LoginBean {
    
    private String name;
    private String psw;
    
    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }

    public void setName(String name){
        this.name = name;
        return;
    }
     
    public void setPsw(String password){
        this.psw = password;
        return;
    }
    
    public String getName(){
        return this.name;
    }
    public String getPsw(){
        return this.psw;
    }
    
    private boolean matching(){
        if( "PAROLASEGRETA".equals(this.psw) ){
            return false;
        }
        else{
            return true;
        }
        
    }
    
    public String check(){
        if( matching() ){
            return "HomeCalendarMonth";
        }
        else
            return "Error";
    }
}
