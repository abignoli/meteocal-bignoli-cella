/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.utility;
//import com.meteocal.business.security.UserManager;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.ejb.EJB;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

/**
 *
 * @author Leo
 *
@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.xhtml"})*/

//                                      implements Filter 
public class AuthFilter_NotLoadingTheme {

//    @EJB
//    UserManager um;
//        
//    public AuthFilter_NotLoadingTheme() {
//    }
// 
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//         
//    }
// 
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain){
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//        HttpSession ses = req.getSession(false);
//        
//        String reqURI = req.getRequestURI();
//        System.out.println("URI: " + reqURI);
//        System.out.println("URL: " + req.getRequestURL());
//        try {
//            if ( reqURI.contains("Index.xhtml") ){
//                System.out.println("The Index page is always accessible!");
//                System.out.println("The desired page for a logged user has to be checked");  
//              
//                res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
//                res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
//                res.setDateHeader("Expires", 0); // Proxies.
//        
//                chain.doFilter(request, response);
//            }
//            else
//                res.sendRedirect(req.getContextPath() + "/Index.xhtml");
//
//        }
//        catch (IOException ex) {
//            Logger.getLogger(AuthFilter_NotLoadingTheme.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        catch (ServletException ex) {
//            Logger.getLogger(AuthFilter_NotLoadingTheme.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    } //doFilter
// 
//    @Override
//    public void destroy() {
//         
//    } 
}