/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.dao;

import com.meteocal.business.entities.User;
import com.meteocal.business.entities.shared.TableDictionary;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;

/**
 *
 * @author Andrea Bignoli
 */

@Stateless
public class UserDAO extends DAObase<User> {
    
    public UserDAO() {
	super(User.class, TableDictionary.TABLE_USER);
    }
    
    public User findByUsername(String username) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("username", username);
        String query = "select u from " + TableDictionary.TABLE_USER + " u where u.username = :username";
        return super.findSingleResult(query, parameters);
    }
}
