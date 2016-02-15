/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.entities.converters;

import com.meteocal.business.entities.shared.WeatherCondition;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.EnumSet;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author Andrea Bignoli, inspired from code by Steven Gertiser
 */
@Converter(autoApply = true)
public class EnumSetWeatherConditionPersistenceConverter implements
        AttributeConverter<EnumSet<WeatherCondition>, Long> {

    @Override
    public Long convertToDatabaseColumn(EnumSet<WeatherCondition> entityValue) {
        if (entityValue == null) {
            return null;
        }

        return WeatherCondition.encodeSet(entityValue);
    }

    @Override
    public EnumSet<WeatherCondition> convertToEntityAttribute(Long databaseValue) {
        if (databaseValue == null) 
            return null;

        return WeatherCondition.decodeSet(databaseValue);
    }
}
