/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.utility;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Leo
 */
@ManagedBean
@RequestScoped
public class AutoCompleter {
    
    public List<String> completeText(String query) {
        List<String> results = new ArrayList<String>();
        results.add(query);
        for (int i = 0; i < 10; i++) {
            results.add(query + i);
        }
        return results;
    }
}
