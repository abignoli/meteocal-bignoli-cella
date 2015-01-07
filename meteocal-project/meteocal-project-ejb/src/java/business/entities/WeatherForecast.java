/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Andrea Bignoli
 */
@Entity
@Table(name = "WEATHERFORECAST")
public class WeatherForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="EVENTID", referencedColumnName = "ID")
    private Event event;
    
    // TODO add start and end datetime
    
    // TODO add weather condition enum
    
    @Column(columnDefinition = "boolean default true")
    private boolean inUse;
}
