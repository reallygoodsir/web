package org.cars.servlets;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PageRedirectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ServletContext servletContext = getServletContext();
        servletContext.log("Start PageRedirectServlet redirection");
        String site = "https://football.ua/";
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", site);
        servletContext.log("End PageRedirectServlet redirection");
    }
}
