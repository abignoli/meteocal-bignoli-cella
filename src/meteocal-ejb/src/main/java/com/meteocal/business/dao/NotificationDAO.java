/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.dao;

import com.meteocal.business.entities.Notification;
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
public class NotificationDAO extends DAObase<Notification> {
    
    public NotificationDAO() {
	super(Notification.class, TableDictionary.TABLE_NOTIFICATION);
    }
}
