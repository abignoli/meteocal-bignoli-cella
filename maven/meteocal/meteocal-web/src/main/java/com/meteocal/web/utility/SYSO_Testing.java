/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.utility;

/**
 *
 * @author Leo
 */
public class SYSO_Testing {
    private static final boolean testing = true;
    
    public static void syso(String stringToPrint){
        if(testing)
            System.out.println(stringToPrint);
    }
    
    public static void clean(){
        for(int i = 0 ; i < 10 ; i++)
            System.out.println(" ");
    }
}
