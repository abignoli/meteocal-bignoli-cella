/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.geography;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.json.stream.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

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
        
        parseWorld();
    }
    
    
    private void parseWorld() {
        
        logger.log(Level.INFO, "BEGIN - Parsing world structure");
        // TODO parse file
        
        ObjectMapper mapper = new ObjectMapper();
        
        InputStream worldData = getClass().getResourceAsStream("/geography/world.json");
        
        try {
            world = mapper.readValue(worldData, World.class);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        logger.log(Level.INFO, "END - Parsing world structure");
    }
    
    public World getWorld() {
        return world;
    }
    
    /**
     * Returns the list of the names of the countries in the application geographic repository.
     * 
     * @return the list of the names of the countries in the application geographic repository
     */
    public List<String> getCountryNames() {
        List<String> names = new ArrayList<String>();
        
        for(Country c: world.getCountry()) {
            names.add(c.getName());
        }
                
        return names;
    }
    
    /**
     * Returns the list of the names of the cities in a given country, identified by countryName.
     * 
     * @param countryName
     * @return the list of the names of the cities in a given country, identified by countryName. Null is returned if no Country with the given name is found
     */
    public List<String> getCityNames(String countryName) {
        List<String> names = null;
        
        for(Country country: world.getCountry()) {
            if(country.getName().equals(countryName)) {
                names = new ArrayList<String>();
                
                for(City city: country.getCity()) {
                    names.add(city.getName());
                }
            }
        }
                
        return names;
    }
}
