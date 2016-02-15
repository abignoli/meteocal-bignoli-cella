/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.shared.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Andrea Bignoli
 */
public class RegExUtils {
    
    public static List<String> decomposeMultiple(String input, String pattern, int interestingGroup) {
        Matcher m = Pattern.compile(pattern).matcher(input);
        List<String> result = new ArrayList<String>();
        
        while (m.find()) {
            result.add(m.group(interestingGroup));
        }
        
        return result;
    }
    
    public static boolean match(String input, String pattern) {
        return Pattern.compile(input).matcher(pattern).matches();
    }    
    
}
