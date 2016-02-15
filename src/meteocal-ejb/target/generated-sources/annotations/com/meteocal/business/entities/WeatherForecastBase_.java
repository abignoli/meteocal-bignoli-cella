package com.meteocal.business.entities;

import com.meteocal.business.entities.shared.WeatherCondition;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-25T23:45:25")
@StaticMetamodel(WeatherForecastBase.class)
public class WeatherForecastBase_ { 

    public static volatile SingularAttribute<WeatherForecastBase, LocalDateTime> forecastEnd;
    public static volatile SingularAttribute<WeatherForecastBase, WeatherCondition> weatherCondition;
    public static volatile SingularAttribute<WeatherForecastBase, LocalDateTime> forecastStart;

}