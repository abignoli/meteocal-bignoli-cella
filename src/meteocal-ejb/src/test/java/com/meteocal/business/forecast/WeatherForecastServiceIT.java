/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.forecast;

import com.meteocal.business.entities.WeatherForecastBase;
import com.meteocal.business.exceptions.InvalidInputException;
import com.meteocal.geography.GeographicRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openweathermap.api.long_range.LongRangeForecast;
import org.openweathermap.api.short_range.ShortRangeForecast;

/**
 *
 * @author Andrea Bignoli
 */
@RunWith(Arquillian.class)
public class WeatherForecastServiceIT {

    @EJB
    WeatherForecastService weatherForecastService;

    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(WeatherForecastService.class)
                .addPackage(WeatherForecastBase.class.getPackage())
                .addPackage(GeographicRepository.class.getPackage())
                .addPackage(LongRangeForecast.class.getPackage())
                .addPackage(ShortRangeForecast.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml");
    }

    @Test
    public void WeatherForecastServiceShouldBeInjected() {
        assertNotNull(weatherForecastService);
    }

    @Test
    public void WeatherForecastServiceProvidesForecasts() {
        List<WeatherForecastBase> forecasts = null;

        LocalDateTime start;
        LocalDateTime end;

        start = LocalDateTime.now().plusDays(1);
        end = LocalDateTime.now().plusDays(1).plusHours(4);

        try {
            forecasts = weatherForecastService.askForecast(start, end, "Milan", "ITALY");
        } catch (InvalidInputException ex) {
            Logger.getLogger(WeatherForecastServiceIT.class.getName()).log(Level.SEVERE, null, ex);
        }

        assertNotNull(forecasts);

        assertTrue(forecasts.size() > 0);

        assertTrue(forecasts.get(0).getForecastStart().equals(start) && forecasts.get(forecasts.size() - 1).getForecastEnd().equals(end));
    }

    @Test
    public void WeatherForecastServiceProvidesForecastsForAllRanges() {
        List<WeatherForecastBase> forecasts = null;

        LocalDateTime start;
        LocalDateTime end;

        start = LocalDateTime.now().plusDays(1);
        end = LocalDateTime.now().plusDays(1).plusHours(4);

        try {
            // S, E < RS
            start = LocalDateTime.now().minusDays(10);
            end = LocalDateTime.now().minusDays(4).minusHours(2);

            forecasts = weatherForecastService.askForecast(start, end, "Milan", "ITALY");

            assertNotNull(forecasts);

            assertTrue(forecasts.size() > 0);

            assertTrue(forecasts.get(0).getForecastStart().equals(start) && forecasts.get(forecasts.size() - 1).getForecastEnd().equals(end));

            // S < RS, RS < E < RM");

            start = LocalDateTime.now().minusHours(10);
            end = LocalDateTime.now().plusDays(weatherForecastService.SHORT_RANGE_DAYS).minusHours(20);

            forecasts = weatherForecastService.askForecast(start, end, "Milan", "ITALY");

            assertNotNull(forecasts);

            assertTrue(forecasts.size() > 0);

            assertTrue(forecasts.get(0).getForecastStart().equals(start) && forecasts.get(forecasts.size() - 1).getForecastEnd().equals(end));
            // S < RS, RM < E < RE");

            start = LocalDateTime.now().minusHours(10);
            end = LocalDateTime.now().plusDays(weatherForecastService.LONG_RANGE_DAYS).minusHours(20);

            forecasts = weatherForecastService.askForecast(start, end, "Milan", "ITALY");

            assertNotNull(forecasts);

            assertTrue(forecasts.size() > 0);

            assertTrue(forecasts.get(0).getForecastStart().equals(start) && forecasts.get(forecasts.size() - 1).getForecastEnd().equals(end));
            // S < RS, RE < E");

            start = LocalDateTime.now().minusHours(10);
            end = LocalDateTime.now().plusDays(weatherForecastService.LONG_RANGE_DAYS + 4);

            forecasts = weatherForecastService.askForecast(start, end, "Milan", "ITALY");

            assertNotNull(forecasts);

            assertTrue(forecasts.size() > 0);

            assertTrue(forecasts.get(0).getForecastStart().equals(start) && forecasts.get(forecasts.size() - 1).getForecastEnd().equals(end));
            // RS < S < RM, RS < E < RM");

            start = LocalDateTime.now().plusDays(weatherForecastService.SHORT_RANGE_DAYS).minusHours(20);
            end = LocalDateTime.now().plusDays(weatherForecastService.SHORT_RANGE_DAYS).minusHours(10);

            forecasts = weatherForecastService.askForecast(start, end, "Milan", "ITALY");

            assertNotNull(forecasts);

            assertTrue(forecasts.size() > 0);

            assertTrue(forecasts.get(0).getForecastStart().equals(start) && forecasts.get(forecasts.size() - 1).getForecastEnd().equals(end));
            // RS < S < RM, RM < E < RE");

            start = LocalDateTime.now().plusDays(weatherForecastService.SHORT_RANGE_DAYS).minusHours(20);
            end = LocalDateTime.now().plusDays(weatherForecastService.LONG_RANGE_DAYS).minusHours(20);

            forecasts = weatherForecastService.askForecast(start, end, "Milan", "ITALY");

            assertNotNull(forecasts);

            assertTrue(forecasts.size() > 0);

            assertTrue(forecasts.get(0).getForecastStart().equals(start) && forecasts.get(forecasts.size() - 1).getForecastEnd().equals(end));
            // RS < S < RM, E > RE");

            start = LocalDateTime.now().plusDays(weatherForecastService.SHORT_RANGE_DAYS).minusHours(20);
            end = LocalDateTime.now().plusDays(weatherForecastService.LONG_RANGE_DAYS + 4);

            forecasts = weatherForecastService.askForecast(start, end, "Milan", "ITALY");

            assertNotNull(forecasts);

            assertTrue(forecasts.size() > 0);

            assertTrue(forecasts.get(0).getForecastStart().equals(start) && forecasts.get(forecasts.size() - 1).getForecastEnd().equals(end));
            // RM < S < RE, RM < E < RE");

            start = LocalDateTime.now().plusDays(weatherForecastService.LONG_RANGE_DAYS).minusHours(20);
            end = LocalDateTime.now().plusDays(weatherForecastService.LONG_RANGE_DAYS).minusHours(10);

            forecasts = weatherForecastService.askForecast(start, end, "Milan", "ITALY");

            assertNotNull(forecasts);

            assertTrue(forecasts.size() > 0);

            assertTrue(forecasts.get(0).getForecastStart().equals(start) && forecasts.get(forecasts.size() - 1).getForecastEnd().equals(end));
            // RM < S < RE, RE < E");

            start = LocalDateTime.now().plusDays(weatherForecastService.LONG_RANGE_DAYS).minusHours(20);
            end = LocalDateTime.now().plusDays(weatherForecastService.LONG_RANGE_DAYS + 4);

            forecasts = weatherForecastService.askForecast(start, end, "Milan", "ITALY");

            assertNotNull(forecasts);

            assertTrue(forecasts.size() > 0);

            assertTrue(forecasts.get(0).getForecastStart().equals(start) && forecasts.get(forecasts.size() - 1).getForecastEnd().equals(end));
            // RE < S,E
            
            start = LocalDateTime.now().plusDays(weatherForecastService.LONG_RANGE_DAYS + 4);
            end = LocalDateTime.now().plusDays(weatherForecastService.LONG_RANGE_DAYS + 6).plusHours(2);

            forecasts = weatherForecastService.askForecast(start, end, "Milan", "ITALY");

            assertNotNull(forecasts);

            assertTrue(forecasts.size() > 0);

            assertTrue(forecasts.get(0).getForecastStart().equals(start) && forecasts.get(forecasts.size() - 1).getForecastEnd().equals(end));
        } catch (InvalidInputException ex) {
            Logger.getLogger(WeatherForecastServiceIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
