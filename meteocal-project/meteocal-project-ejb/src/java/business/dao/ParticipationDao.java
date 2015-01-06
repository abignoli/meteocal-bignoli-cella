/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.dao;

import business.entities.Event;
import business.entities.User;
import javax.ejb.Stateless;

/**
 *
 * @author USUARIO
 */
@Stateless
public class ParticipationDao {
    
    public void testCreatePartecipation(User u, Event e) {
        u.partecipateTo(e);
    }
}
