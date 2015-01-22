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
public class GeograficRepository {
    
    private static final Logger logger = Logger.getLogger(GeograficRepository.class.getName());
    
    private World world;
    
    @PostConstruct
    private void constructed() {
        logger.log(Level.INFO, "GeograficRepository created - parsing world structure");
        world = parseWorld();
    }
    
    
    private static World parseWorld() {
        logger.log(Level.INFO, "Parsing world structure");
        // TODO parse file
        
        return null;
    }
    
    public World getWorld() {
        return world;
    }
}
