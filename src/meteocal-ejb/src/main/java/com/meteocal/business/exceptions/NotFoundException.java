/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.exceptions;

/**
 *
 * @author Andrea Bignoli
 */
public class NotFoundException extends BusinessException {
    
    public static final String EVENT_NOT_FOUND = "[DATABASE QUERY] The requested event doesn't exist in the database";
    public static final String USER_NOT_FOUND = "[DATABASE QUERY] The requested user doesn't exist in the database";
    public static final String GENERIC_NOT_FOUND = "[DATABASE QUERY] The requested entity doesn't exist in the database";

    /**
     * Creates a new instance of <code>NotFound</code> without detail message.
     */
    public NotFoundException() {
    }

    /**
     * Constructs an instance of <code>NotFound</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public NotFoundException(String msg) {
        super(msg);
    }
}
