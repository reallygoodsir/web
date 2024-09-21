package org.cars.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PageRedirectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ServletContext servletContext = getServletContext();
        ServletConfig servletConfig = getServletConfig();
        String footballRedirectUrl = servletConfig.getInitParameter("football-redirect-url");
        servletContext.log("Start PageRedirectServlet redirection");
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", footballRedirectUrl);
        servletContext.log("End PageRedirectServlet redirection");
    }
}
