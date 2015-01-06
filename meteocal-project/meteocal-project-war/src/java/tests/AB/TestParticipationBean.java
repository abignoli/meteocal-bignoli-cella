/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.AB;

import business.entities.Event;
import business.entities.User;
import business.tests.ParticipationTest;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Andrea Bignoli
 */
@ManagedBean
@RequestScoped
public class TestParticipationBean {
    
    @EJB
    private ParticipationTest testCreatePartecipationEJB;

    /**
     * Creates a new instance of TestParticipation
     */
    public TestParticipationBean() {
        
    }
    
    public void testCreateParticipation() {
        testCreatePartecipationEJB.testCreateParticipation();
    }
    
}
