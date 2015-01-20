/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.utility;

import com.meteocal.business.entities.User;
import com.meteocal.business.security.UserManager;
import com.meteocal.business.shared.security.UserEventVisibility;
import static com.meteocal.business.shared.security.UserEventVisibility.CREATOR;
import static com.meteocal.business.shared.security.UserEventVisibility.VIEWER;
import com.meteocal.web.filters.FilterEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Leo
 */
public class EventPage extends HttpServlet {

    @EJB
    private UserManager um;
       
    @Inject
    private SessionUtility sessionUtility;

    private User loggedUser;
   
    
    public EventPage(){
    }

    private void setUser(User user) {
        this.loggedUser = user;
    }    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        this.setUser(um.getLoggedUser());
        final String indexPath =  "/Index.xhtml";
        final String creatorPath =  "/WEB-INF/HiddenPages/EventPageCreator.xhtml";
        final String viewerPath =  "/WEB-INF/HiddenPages/EventPageViewer.xhtml";
        final String noVisibilityPath =  "/WEB-INF/HiddenPages/EventPageNoVisibility.xhtml";

        //eventID = SessionUtility.getQueryString().split("=")[1];
        int eventID = 1;
        boolean comingFromRedirect;
        UserEventVisibility visibility;//one of: CREATOR, VIEWER, NO_VISIBILITY


        String relativePath = request.getRequestURI().replace("/meteocal-web", "");

        SYSO_Testing.syso("URI: " + request.getRequestURI());
        SYSO_Testing.syso("relativePath: " + relativePath);

        try {
//            request.getRequestDispatcher("/WEB-INF/HiddenPages/EventPageCreator.xhtml").forward(request, response);

            if (isNotLogged()) {
                SYSO_Testing.syso("I'm not logged");
                response.sendRedirect(indexPath);
                SYSO_Testing.syso("something wrong!!");
                return;
            }
            else {
                
                String username = loggedUser.getUsername();
////                try {
////                    visibility = um.getVisibilityOverEvent(eventID);
////                }
////                catch (NotFoundException ex) {
////                    Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ex);
////                    SessionUtility.redirect("/Index.xhtml");
////                }
                visibility = CREATOR;
                SYSO_Testing.syso("Username " + username);
                SYSO_Testing.syso("I'm logged, and I've to check the visibility");
              
                if (visibility == CREATOR) {
                    SYSO_Testing.syso("creator");
                    request.getRequestDispatcher("/WEB-INF/HiddenPages/EventPageCreator.xhtml").forward(request, response);
                    return;
                    
//                    sessionUtility.setComingFromDispatcher();
//                    sessionUtility.setEventID(eventID);
//                    try {
//                        SYSO_Testing.syso("pre-dispatcher");
//                        request.getRequestDispatcher(creatorPath).forward(request, response);
//                        SYSO_Testing.syso("post-dispatcher");
//                    }
//                    catch (IOException ex) {
//                        SYSO_Testing.syso("secondTryCatch");
//                        Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                }
                else {
                    if (visibility == VIEWER) {
                        SYSO_Testing.syso("viewer");
//                        sessionUtility.setComingFromDispatcher();
//                        request.getRequestDispatcher(viewerPath).forward(request, response);
                    }
                    else {// NO VISIBILITY
                        SYSO_Testing.syso("no Visibility");
//                        sessionUtility.setComingFromDispatcher();
//                        request.getRequestDispatcher(noVisibilityPath).forward(request, response);  
                    }
                } 
                
//                return;
            }
        }
        catch (NullPointerException ec) {
            Logger.getLogger(FilterEvent.class.getName()).log(Level.SEVERE, null, ec);
        }
    }

    private boolean isNotLogged() {
        if(this.loggedUser==null)SYSO_Testing.syso("User null");
        return !sessionUtility.getLoggedUser().equals(this.loggedUser.getUsername());
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}