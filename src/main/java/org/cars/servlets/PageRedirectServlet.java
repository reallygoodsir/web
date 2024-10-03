package org.cars.servlets;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/page-redirect", initParams = {
        @WebInitParam(name = "football-redirect-url", value = "https://football.ua/")
})
public class PageRedirectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ServletContext servletContext = getServletContext();
        String footballRedirectUrl = getInitParameter("football-redirect-url");
        servletContext.log("Start PageRedirectServlet redirection");
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", footballRedirectUrl);
        servletContext.log("End PageRedirectServlet redirection");
    }
}
