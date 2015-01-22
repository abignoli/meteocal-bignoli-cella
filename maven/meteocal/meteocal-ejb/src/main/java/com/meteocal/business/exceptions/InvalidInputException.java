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
    public static String EVENT_CHANGE_INVALID_STATE = "[EVENT VALIDATION] Only events in 'PLANNED' state can be modified";
    
    public static String USER_ALREADY_INVITED = "[EVENT INVITATION] User is already invited";
    
    public static String EVENT_GOOD_WEATHER_CONDITIONS_CHECK = "[EVENT - GOOD WEATHER CONDITIONS CHECK] No weather conditions, can't check compatibility.";
    public static String EVENT_CREATION_INVALID = "[EVENT CREATION] Provided data is invalid";
    public static String EVENT_CREATION_NO_CITY_OR_COUNTRY = "[EVENT CREATION] Cannot create an event without specifying both city and country.";
    public static String EVENT_CREATION_NO_LOGGED_USER = "[EVENT CREATION] No user is logged in, cannot create the event.";
    
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
