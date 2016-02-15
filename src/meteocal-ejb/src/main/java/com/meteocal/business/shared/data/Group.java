/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.shared.data;

/**
 *
 * @author Andrea Bignoli
 */
public enum Group {
    USER("USER");

    private final String name;
    
    Group(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
