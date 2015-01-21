/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.dao;

import com.meteocal.business.entities.NotificationView;
import com.meteocal.business.entities.shared.TableDictionary;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;

/**
 *
 * @author USUARIO
 */
@Stateless
public class NotificationViewDAO extends DAObase<NotificationView> {
    
    public NotificationViewDAO() {
	super(NotificationView.class, TableDictionary.TABLE_NOTIFICATION_VIEW);
    }
    
//    public long getNotSeenCount(int userID) {
//        Map<String, Object> parameters = new HashMap<String, Object>();
//        parameters.put("userid", userID);
//        String query = "select COUNT(nv.notificationid) from NotificationView nv where nv.userid = :userid and nv.seen = false";
//        return (long) super.findSingleResult(query, parameters);
//    }
}
