/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.dao;

import com.meteocal.business.entities.Invitation;
import com.meteocal.business.entities.shared.TableDictionary;
import javax.ejb.Stateless;

/**
 *
 * @author Andrea Bignoli
 */
@Stateless
public class InvitationDao extends DAObase<Invitation> {

    public InvitationDao() {
        super(Invitation.class, TableDictionary.TABLE_INVITATION);
    }
    

}
