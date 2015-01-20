/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.tests.AB;

import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author ate
 */
@ManagedBean
@RequestScoped
public class TestReferencedBean {
    
    private String testString = "My teststring!";

    /**
     * Creates a new instance of TestReferencedBean
     */
    public TestReferencedBean() {
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }
    
    public void testFunction() {
        System.out.println("Test function has been called!");
    }
    
    
    
}
