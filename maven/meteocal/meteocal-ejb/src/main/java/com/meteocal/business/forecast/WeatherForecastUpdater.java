/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.forecast;

import com.meteocal.business.entities.WeatherForecastBase;
import com.meteocal.business.shared.scheduling.ScheduleActivator;
import com.meteocal.shared.ApplicationVariables;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import org.openweathermap.api.long_range.LongRangeForecast;
import org.openweathermap.api.short_range.ShortRangeForecast;

/**
 *
 * @author Andrea Bignoli
 */
@Singleton
public class WeatherForecastUpdater {

    private static final Logger logger = Logger.getLogger(WeatherForecastUpdater.class.getName());
    
    private Client client;
    
    private final String OPENWEATHERMAP_API_SHORT_RANGE_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?q={0},{1}&mode=json&APPID={2}";    
    private final String OPENWEATHERMAP_API_LONG_RANGE_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q={0},{1}&cnt=16&mode=json&APPID={2}";  
    
    @PostConstruct
    private void constructed() {
        logger.log(Level.INFO, "Weather Forecast Updater created");
        client = ClientBuilder.newClient();
    }

    @Schedule(second = "*/10", minute = "*", hour = "*", persistent = false)
    public void testTask() {
        if(ScheduleActivator.WEATHER_FORECAST_UPDATER_TEST_TASK) {
            logger.log(Level.INFO, "{0}: scheduled task is getting fired", LocalDateTime.now());
//            logger.log(Level.INFO, "{0}: checking the weather", new Date());
//            Forecast forecast = client.target("http://localhost:8080/weather/rest/forecast")
//                    .request(MediaType.APPLICATION_JSON)
//                    .get(Forecast.class);
//            logger.log(Level.INFO, "Oracle says: {0}", forecast.getResult());
        }
    }
    
    public void testShortRangeRequest() {
        logger.log(Level.INFO, "{0}: asking short range forecast", LocalDateTime.now());
        ShortRangeForecast shortRange = client.target(getShortRangeRequestURL("Monza","IT")).request(MediaType.APPLICATION_JSON).get(ShortRangeForecast.class);
        logger.log(Level.INFO, "End of forecast request");
    }
    
    public List<WeatherForecastBase> askForecast(LocalDateTime start, LocalDateTime end, String city, String countryID) {
        ShortRangeForecast shortRangeForecast;
        LongRangeForecast longRangeForecast;
        
        if(isShortRangeForecastNeeded(start, end))
            shortRangeForecast = getShortRangeForecast(city, countryID);
        if(isLongRangeForecastNeeded(start, end))
            longRangeForecast = getLongRangeForecast(city, countryID);
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private String getShortRangeRequestURL(String city, String countryID) {
        return MessageFormat.format(OPENWEATHERMAP_API_SHORT_RANGE_BASE_URL, city, countryID, ApplicationVariables.OPENWEATHERMAP_API_KEY);
    }
    
    private String getLongRangeRequestURL(String city, String countryID) {
        return MessageFormat.format(OPENWEATHERMAP_API_LONG_RANGE_BASE_URL, city, countryID, ApplicationVariables.OPENWEATHERMAP_API_KEY);
    }
    
    private ShortRangeForecast getShortRangeForecast(String city, String countryID) {
        return client.target(getShortRangeRequestURL(city,countryID)).request(MediaType.APPLICATION_JSON).get(ShortRangeForecast.class);
    }
    
    private LongRangeForecast getLongRangeForecast(String city, String countryID) {
        return client.target(getLongRangeRequestURL(city,countryID)).request(MediaType.APPLICATION_JSON).get(LongRangeForecast.class);
    }

    private boolean isShortRangeForecastNeeded(LocalDateTime start, LocalDateTime end) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean isLongRangeForecastNeeded(LocalDateTime start, LocalDateTime end) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private enum ForecastType {
        SHORT_RANGE, LONG_RANGE
    }
}
