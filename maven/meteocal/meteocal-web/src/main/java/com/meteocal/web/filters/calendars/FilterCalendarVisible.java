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
import com.meteocal.business.shared.security.UserUserVisibility;
import com.meteocal.web.utility.SessionUtility;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Leo
 */
@RequestScoped
@ManagedBean
public class FilterCalendarVisible {

    @EJB
    private UserManager um;

    @EJB
    private UserFacade uf;

    @Inject
    private SessionUtility sessionUtility;

    private HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    private final String errorOutcome = "Error";
    private final String visibleOutcome = "UserVisible";
    private final String noVisibleOutcome = "UserNoVisible";
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
        String userB;
        UserUserVisibility visibility;

        userB = getUser();

        try {
            visibility = um.getVisibilityOverUser(uf.findByUsername(userB).getId());
        }
        catch (NotFoundException ex) {
            FacesContext fc = FacesContext.getCurrentInstance();
            String contextPath = request.getContextPath();
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/Index.xhtml?faces-redirect=true");
            }
            catch (IOException ex1) {
                Logger.getLogger(FilterCalendarVisible.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return;
        }

        if (visibility == UserUserVisibility.NOT_VISIBLE) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, noVisibleOutcome);
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
