/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.filters.calendars;

import com.meteocal.business.entities.User;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.facade.UserFacade;
import com.meteocal.business.security.UserManager;
import com.meteocal.business.shared.security.UserEventVisibility;
import static com.meteocal.business.shared.security.UserEventVisibility.CREATOR;
import com.meteocal.business.shared.security.UserUserVisibility;
import com.meteocal.web.beans.component.ErrorBean;
import com.meteocal.web.exceptions.NotValidParameter;
import com.meteocal.web.filters.events.FilterEvent;
import com.meteocal.web.utility.SYSO_Testing;
import com.meteocal.web.utility.SessionUtility;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Leo
 */
public class FilterCalendarVisible {

    @EJB
    private UserManager um;

    @EJB
    private UserFacade uf;

    @Inject
    private SessionUtility sessionUtility;

    private HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    private final String errorOutcome = "Error";
    private final String visibleOutcome = "Visible";
    private final String noVisibleOutcome = "NoVisible";
    private User loggedUser;

    @PostConstruct
    public void init() {
        this.setUser(um.getLoggedUser());
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    private void setUser(User user) {
        this.loggedUser = user;
    }
    

    public void check(ComponentSystemEvent event) {
        String userA, userB;
        UserUserVisibility visibility;

        userB = getUser();
        try {
            visibility = um.getVisibilityOverUser(uf.findByUsername(userB).getId());
        }
        catch (NotFoundException ex) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, errorOutcome);
            return;
        }

        if (visibility == UserUserVisibility.NOT_VISIBLE) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, noVisibleOutcome);
            return;
        }
    }

    private boolean isNotLogged() {
        if (this.loggedUser == null) {
            return true;
        }
        return !sessionUtility.getLoggedUser().equals(this.loggedUser.getUsername());
    }

    private String getUser() {
        String user;

        if (!sessionUtility.isAUser()) {
            user = request.getParameter("username");
        }
        else {
            user = sessionUtility.getLoggedUser();
        }

        return user;
    }
}
