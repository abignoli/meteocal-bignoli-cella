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
public class BusinessException extends Exception {
    
    public static String MISSING_PASSWORD = "[SECURITY] Current password is required to perform this operation.";
    public static String WRONG_PASSWORD = "[SECURITY] The given password doesn't match the current database entry.";

    /**
     * Creates a new instance of <code>BusinessException</code> without detail
     * message.
     */
    public BusinessException() {
    }

    /**
     * Constructs an instance of <code>BusinessException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public BusinessException(String msg) {
        super(msg);
    }
}
