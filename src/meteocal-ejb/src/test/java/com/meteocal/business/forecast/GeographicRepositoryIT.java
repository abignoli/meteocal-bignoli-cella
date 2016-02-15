/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.forecast;

import com.meteocal.geography.City;
import com.meteocal.geography.Country;
import com.meteocal.geography.GeographicRepository;
import com.meteocal.geography.World;
import java.util.List;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Andrea Bignoli
 */
@RunWith(Arquillian.class)
public class GeographicRepositoryIT {
    
    @EJB
    GeographicRepository geographicRepository;
    
    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackage(GeographicRepository.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml");
    }
    
    @Test
    public void WeatherForecastServiceShouldBeInjected() {
        assertNotNull(geographicRepository);
    }
    
    @Test
    public void WeatherForecastShouldProvideAWorldStructure() {
        assertNotNull(geographicRepository.getWorld());
    }
}
