/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.utility;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Leo
 */
@WebServlet
public class HighestPriorityServlet_LogicPattern extends HttpServlet {
    
    private String relativePath;
    
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
        
        SYSO_Testing.syso("in HighestPriorityServlet: processRequest");
        SYSO_Testing.syso("URI: " +request.getRequestURI());
        SYSO_Testing.syso("URL: " + request.getRequestURL());
        SYSO_Testing.syso("server name: " + request.getServerName());
        SYSO_Testing.syso("replace: " + request.getRequestURI().replace("/meteocal-web",""));
        SYSO_Testing.syso("pathInfo: " + request.getPathInfo());
        
        relativePath = request.getRequestURI().replace("/meteocal-web","");
        
        if("/protected/personal/Settings.xhtml".equals(relativePath))
            SYSO_Testing.syso("Match!");
        
        request.getRequestDispatcher("/protected/personal/Settings.xhtml").forward(request, response);
        
        
        if(isNotLogged()) {
            SYSO_Testing.syso("I'm not logged");
            request.getRequestDispatcher(relativePath).forward(request, response);
        } else {
            SYSO_Testing.syso("I'm logged, and I've to check the visibility");
            if( logica() ){
                request.getRequestDispatcher(relativePath).forward(request, response);
            }else{
                request.getRequestDispatcher(relativePath).forward(request, response);
            }
        }
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
    
    private boolean logica(){
        int a=(int) (Math.random() * 10);
        return a%2 == 0;
    }
    
    private boolean isNotLogged(){
        return logica();
    }
}
