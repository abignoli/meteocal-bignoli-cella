/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.facade;

import business.entities.User;
import business.exceptions.BusinessException;
import javax.ejb.Local;

/**
 *
 * @author Andrea Bignoli
 */
@Local
public interface UserFacade {

    public abstract void save(User u);

    public abstract void update(User u) throws BusinessException;
    
    public abstract void update(User u, String oldPassword) throws BusinessException;

    public abstract void remove(User u);

    public abstract User findByUsername(String username);

}
