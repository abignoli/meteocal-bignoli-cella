/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.filters.components;

import com.meteocal.web.utility.SessionUtility;
import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Leonardo Cella
 */
@Named
@RequestScoped
public class NotLoggedFilter {
    
    @Inject
    SessionUtility sessionUtility;
    
    public void check() throws IOException {
        if (sessionUtility.getLoggedUser() == null) {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.sendRedirect("/Error.xhtml");
        }
    }
}
