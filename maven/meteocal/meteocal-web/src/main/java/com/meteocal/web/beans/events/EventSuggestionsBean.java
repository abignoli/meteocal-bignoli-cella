/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.events;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.time.LocalDateTime;

/**
 *
 * @author Leo
 */
@RequestScoped
@Named
public class EventSuggestionsBean implements Serializable{
    private LocalDateTime start,end;
    private String name;
    
    public EventSuggestionsBean(){
    }

    public String eventEditing(){
        return "/protected/event/EventPageCreator.xhtml?";
    }
    
    public LocalDateTime getStart() {
        return start;
    }
    public void setStart(LocalDateTime start){
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void accept(){
        
    }
    
    public void discard(){
    
    }
}
