/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.entities.keys;

import java.io.Serializable;

/**
 *
 * @author Andrea Bignoli
 */
public class InvitationID implements Serializable {
    private int userID;
    private int eventID;

    public InvitationID() {
    }

    public InvitationID(int userID, int eventID) {
        this.userID = userID;
        this.eventID = eventID;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.userID;
        hash = 59 * hash + this.eventID;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InvitationID other = (InvitationID) obj;
        if (this.userID != other.userID) {
            return false;
        }
        if (this.eventID != other.eventID) {
            return false;
        }
        return true;
    }
}
