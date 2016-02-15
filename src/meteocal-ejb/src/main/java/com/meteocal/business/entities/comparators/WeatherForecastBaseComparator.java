/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.entities.comparators;

import com.meteocal.business.entities.WeatherForecastBase;
import java.util.Comparator;

/**
 *
 * @author Andrea Bignoli
 */
public class WeatherForecastBaseComparator implements Comparator<WeatherForecastBase> {

    @Override
    public int compare(WeatherForecastBase o1, WeatherForecastBase o2) {
        if (o1 == null || o2 == null) {
            return 0;
        }
        if (o1.getForecastStart().isBefore(o2.getForecastStart())) {
            return -1;
        }
        if (o2.getForecastStart().isBefore(o1.getForecastStart())) {
            return 1;
        }

        return 0;
    }
}
