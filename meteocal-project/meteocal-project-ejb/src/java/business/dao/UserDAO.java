/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.dao;

import business.entities.User;
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
        
}
