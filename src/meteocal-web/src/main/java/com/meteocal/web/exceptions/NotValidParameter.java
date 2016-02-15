/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.exceptions;

/**
 *
 * @author Leonardo Cella
 */
public class NotValidParameter extends Exception{
     
    public static final String MISSING_PARAMETER = "{SECURITY} Not valid parameter in get";
    
    /**
     * Creates a new instance of <code>NotFoundPrameter</code> without detail
     * message.
     */
    public NotValidParameter() {
    }

    /**
     * Constructs an instance of <code>NotFoundParameter</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NotValidParameter(String msg) {
        super(msg);
    }   
}
