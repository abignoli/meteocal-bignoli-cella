/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.converters;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 *
 * @author Leo
 */
public class ConverterLocalDateTimeAndDate {

    public static Date toDate(LocalDateTime d){
        Instant instant = d.toInstant(ZoneOffset.UTC);
        return Date.from(instant);
    }
}
