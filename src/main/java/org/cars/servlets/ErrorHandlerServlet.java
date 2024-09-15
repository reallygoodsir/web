package org.cars.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorHandlerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Inside ErrorHandlerServlet doGet");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        System.out.println("Start ErrorHandlerServlet doPost");

        Throwable throwable = (Throwable)
                request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer)
                request.getAttribute("javax.servlet.error.status_code");
        String servletName = (String)
                request.getAttribute("javax.servlet.error.servlet_name");
        StringBuilder data = new StringBuilder();
        data.append("Message: " + throwable.getMessage()).append("\n");
        data.append("Status Code: " + statusCode).append("\n");
        data.append("Servlet Name: " + servletName).append("\n");
        System.out.println("Exception data " + data);
        response.getWriter().println("<h1>" + throwable.getMessage() + "</h1>");
        response.setStatus(200);

        System.out.println("End ErrorHandlerServlet doPost");
    }
}
