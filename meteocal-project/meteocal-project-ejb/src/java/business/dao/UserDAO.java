/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.dao;

import business.entities.User;
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
	super(User.class);
    }
    
    public User findByUsername(String username) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("username", username);
        String query = "select u from User u where u.username = :username";
        return super.findSingleResult(query, parameters);
    }
}
