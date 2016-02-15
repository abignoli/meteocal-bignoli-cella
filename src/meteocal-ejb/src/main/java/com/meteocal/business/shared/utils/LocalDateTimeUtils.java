/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.shared.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 *
 * @author Andrea Bignoli
 */
public abstract class LocalDateTimeUtils {
    /**
     * 
     * @param unixTime expressed in seconds since January 1 1970 GMT, standard epoch
     * @return 
     */
    public static LocalDateTime convertUnixToLocalDateTime(long unixTime){
        return LocalDateTime.ofEpochSecond(unixTime, 0, ZoneOffset.UTC);
    }
    
    public static boolean inBetween(LocalDateTime middle, LocalDateTime interval_start, LocalDateTime interval_end) {
        return middle.isAfter(interval_start) && middle.isBefore(interval_end);
    }
    
    public static long distance(LocalDateTime start, LocalDateTime end) {
        return end.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC);
    }
    
    public static LocalDateTime add(LocalDateTime localDateTime, long delta) {
        return convertUnixToLocalDateTime(localDateTime.toEpochSecond(ZoneOffset.UTC) + delta);
    }

    public static LocalDateTime subtract(LocalDateTime localDateTime, long delta) {
        return add(localDateTime, -delta);
    }
}
