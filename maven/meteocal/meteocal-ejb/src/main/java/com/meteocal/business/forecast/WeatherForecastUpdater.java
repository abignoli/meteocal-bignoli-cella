/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.forecast;

import com.meteocal.business.shared.scheduling.ScheduleActivator;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import org.openweathermap.api.short_range.ShortRange;

/**
 *
 * @author Andrea Bignoli
 */
@Singleton
public class WeatherForecastUpdater {

    private static final Logger logger = Logger.getLogger(WeatherForecastUpdater.class.getName());
    
    private Client client;
    
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
        ShortRange shortRange = client.target("http://api.openweathermap.org/data/2.5/forecast?q=Monza,it&mode=json&APPID=3b8eccdf67c3fa73d929297e99ed204a").request(MediaType.APPLICATION_JSON).get(ShortRange.class);
        logger.log(Level.INFO, "End of forecast request");
    }
}
