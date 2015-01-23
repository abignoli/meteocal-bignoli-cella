/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.entities.shared;

import java.util.EnumSet;

/**
 *
 * @author AB
 */
public enum WeatherCondition {

    SUN(WeatherConditionCodeDictionary.SUN_CODE),
    RAIN(WeatherConditionCodeDictionary.RAIN_CODE),
    SNOW(WeatherConditionCodeDictionary.SNOW_CODE),
    CLOUDS(WeatherConditionCodeDictionary.CLOUDS_CODE),
    NOT_AVAILABLE(WeatherConditionCodeDictionary.NOT_AVAILABLE_CODE);

    private final int code;

    WeatherCondition(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static WeatherCondition decode(int code) {
        WeatherCondition decoded;

        switch (code) {
            case WeatherConditionCodeDictionary.SUN_CODE:
                decoded = WeatherCondition.SUN;
                break;
            case WeatherConditionCodeDictionary.RAIN_CODE:
                decoded = WeatherCondition.RAIN;
                break;
            case WeatherConditionCodeDictionary.SNOW_CODE:
                decoded = WeatherCondition.SNOW;
                break;
            case WeatherConditionCodeDictionary.CLOUDS_CODE:
                decoded = WeatherCondition.CLOUDS;
                break;
            case WeatherConditionCodeDictionary.NOT_AVAILABLE_CODE:
                decoded = WeatherCondition.NOT_AVAILABLE;
                break;
            default:
                decoded = null;
                break;
        }

        return decoded;
    }

    public static Long encodeSet(EnumSet<WeatherCondition> weatherConditions) {
        long setCode = 0;

        for (WeatherCondition wc : weatherConditions) {
            if (wc != null) {
                setCode += (int) Math.pow(2, wc.getCode());
            }
        }

        return setCode;
    }

    public static EnumSet<WeatherCondition> decodeSet(Long setCode) {
        if (setCode == null) {
            return null;
        }

        EnumSet<WeatherCondition> decoded = EnumSet.noneOf(WeatherCondition.class);
        int binaryIndex = 0;
        WeatherCondition toAdd;

        while (setCode > 0) {
            // If the bit relative to the weather condition pointed by binary Index is true
            if (setCode % 2 == 1) {
                toAdd = decode(binaryIndex);
                if (toAdd != null) {
                    decoded.add(toAdd);
                }
            }

            setCode /= 2;
            binaryIndex++;
        }

        return decoded;
    }
}
