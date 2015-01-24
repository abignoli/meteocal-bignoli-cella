/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.exceptions;

/**
 *
 * @author AB
 */
public class InvalidInputException extends BusinessException {
    
    public static final String EVENT_VALIDATION_NO_START = "[EVENT VALIDATION] Event start must be defined";
    public static final String EVENT_VALIDATION_NO_END = "[EVENT VALIDATION] Event end must be defined";
    public static final String EVENT_START_AFTER_END = "[EVENT VALIDATION] Event should start before end";
    
    public static final String EVENT_CANCEL_INVALID_STATE = "[EVENT VALIDATION] Only events in 'PLANNED' state can be canceled";
    public static final String EVENT_CHANGE_INVALID_STATE = "[EVENT VALIDATION] Only events in 'PLANNED' state can be modified";
    
    public static final String USER_ALREADY_INVITED = "[EVENT INVITATION] User is already invited";
    
    public static final String EVENT_GOOD_WEATHER_CONDITIONS_CHECK = "[EVENT - GOOD WEATHER CONDITIONS CHECK] No weather conditions, can't check compatibility.";
    public static final String EVENT_CREATION_INVALID = "[EVENT CREATION] Provided data is invalid";
    public static final String EVENT_CREATION_NO_CITY_OR_COUNTRY = "[EVENT CREATION] Cannot create an event without specifying both city and country.";
    public static final String EVENT_CREATION_NO_LOGGED_USER = "[EVENT CREATION] No user is logged in, cannot create the event.";
    
    public static final String WEATHER_FORECAST_SERVICE_ASK_FORECAST_START_AFTER_END = "[WEATHER FORECAST SERVICE] Weather forecast request: start after end.";
    public static final String WEATHER_FORECAST_SERVICE_INVALID_BEST_MATCH_CALCULATION = "[WEATHER FORECAST SERVICE] Invalid distance calculation.";

            
    /**
     * Creates a new instance of <code>NotFound</code> without detail message.
     */
    public InvalidInputException() {
    }

    /**
     * Constructs an instance of <code>NotFound</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public InvalidInputException(String msg) {
        super(msg);
    }
}
