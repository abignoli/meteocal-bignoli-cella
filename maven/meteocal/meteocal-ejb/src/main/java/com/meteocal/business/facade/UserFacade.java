/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.entities.User;
import com.meteocal.business.exceptions.BusinessException;
import javax.ejb.Local;

/**
 *
 * @author Andrea Bignoli
 */
@Local
public interface UserFacade {

    public abstract void save(User u);

    public abstract void updateData(User u) throws BusinessException;
    
    public abstract void updateData(User u, String oldPassword) throws BusinessException;

    public abstract void remove(User u);

    public abstract User findByUsername(String username);

}
