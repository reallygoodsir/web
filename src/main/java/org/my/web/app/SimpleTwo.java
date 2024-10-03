package org.my.web.app;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/simple-two", initParams = {
        @WebInitParam(name = "foo", value = "Hello "),
        @WebInitParam(name = "bar", value = " World!")
})
public class SimpleTwo extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws  IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print("<html><body>");
        out.print("<h3>Hello Servlet</h3>");
        out.println(getInitParameter("foo"));
        out.println(getInitParameter("bar"));
        out.print("</body></html>");
    }
}
