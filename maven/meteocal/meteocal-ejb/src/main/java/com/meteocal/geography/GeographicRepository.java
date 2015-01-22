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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private List<String> countryNames;
    private Map<String, List<String>> cityNamesMap;
    
    private Map<String, Country> countryNamesMap;

    @PostConstruct
    private void constructed() {
        logger.log(Level.INFO, "GeograficRepository constructed");

        parseWorld();

        computeCountryNames();
        computeCityNamesMap();
        computeCountryNamesMap();
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
     * Returns the list of the names of the countries in the application
     * geographic repository.
     *
     * @return the list of the names of the countries in the application
     * geographic repository
     */
    public List<String> getCountryNames() {
        return countryNames;
    }

    /**
     * Returns the list of the names of the cities in a given country,
     * identified by countryName.
     *
     * @param countryName
     * @return the list of the names of the cities in a given country,
     * identified by countryName. Null is returned if no Country with the given
     * name is found
     */
    public List<String> getCityNames(String countryName) {
        return cityNamesMap.get(countryName);
    }

    private void computeCountryNames() {
        countryNames = new ArrayList<String>();

        for (Country c : world.getCountry()) {
            countryNames.add(c.getName());
        }
    }

    private void computeCityNamesMap() {
        List<String> cityNames;
        cityNamesMap = new HashMap<String, List<String>>();

        for (Country country : world.getCountry()) {
            cityNames = new ArrayList<String>();

            for (City city : country.getCity()) {
                cityNames.add(city.getName());
            }

            cityNamesMap.put(country.getName(), cityNames);
        }
    }

    private void computeCountryNamesMap() {
        countryNamesMap = new HashMap<String, Country>();
        
        for(Country c: world.getCountry())
            countryNamesMap.put(c.getName(), c);
    }
    
    public String getCountryID(String countryName) {
        Country country = countryNamesMap.get(countryName);
        
        if(country == null)
            return null;
        
        return country.getId();
    }
}
