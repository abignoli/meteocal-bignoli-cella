/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.geography;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

/**
 *
 * @author AB
 */
@Singleton
public class GeographicRepository {
    
    private static final Logger logger = Logger.getLogger(GeographicRepository.class.getName());
    
    private World world;
    
    @PostConstruct
    private void constructed() {
        logger.log(Level.INFO, "GeograficRepository constructed");
        world = parseWorld();
    }
    
    
    private static World parseWorld() {
        logger.log(Level.INFO, "Parsing world structure");
        // TODO parse file
        
        ObjectMapper mapper = new ObjectMapper();
        
        return null;
    }
    
    public World getWorld() {
        return world;
    }
}
