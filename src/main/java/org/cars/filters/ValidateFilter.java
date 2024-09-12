package org.cars.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ValidateFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getMethod().equalsIgnoreCase("GET")) {
            filterChain.doFilter(servletRequest, response);
        } else {
            String id = servletRequest.getParameter("id");
            boolean idEmpty = id == null || id.isEmpty();
            String type = servletRequest.getParameter("type");
            boolean typeEmpty = type == null || type.isEmpty();
            String price = servletRequest.getParameter("price");
            boolean priceEmpty = price == null || price.isEmpty();
            String name = servletRequest.getParameter("name");
            boolean nameEmpty = name == null || name.isEmpty();
            String model = servletRequest.getParameter("model");
            boolean modelEmpty = model == null || model.isEmpty();
            String make = servletRequest.getParameter("make");
            boolean makeEmpty = make == null || make.isEmpty();

            StringBuilder html = new StringBuilder();
            html.append("<h1>Add New Car</h1>\n" +
                    "<form method=\"POST\">\n");
            boolean anyEmpty = false;
            if (idEmpty) {
                html.append("<h3 style=\"color: red;\">Please enter ID</h3>");
                anyEmpty = true;
            }
            if (nameEmpty) {
                html.append("<h3 style=\"color: red;\">Please enter name</h3>");
                anyEmpty = true;
            }
            if (makeEmpty) {
                html.append("<h3 style=\"color: red;\">Please enter make</h3>");
                anyEmpty = true;
            }
            if (modelEmpty) {
                html.append("<h3 style=\"color: red;\">Please enter model</h3>");
                anyEmpty = true;
            }
            if (priceEmpty) {
                html.append("<h3 style=\"color: red;\">Please enter price</h3>");
                anyEmpty = true;
            }
            if (typeEmpty) {
                html.append("<h3 style=\"color: red;\">Please enter type</h3>");
                anyEmpty = true;
            }
            if (anyEmpty) {
                html.append("  <h3>ID:     <input type=\\\"text\\\" name=\"id\" value=\"" + id + "\" /> <h3>" +
                        "  <h3>Name:   <input type=\\\"text\\\" name=\"name\" value=\"" + name + "\" /> <h3>" +
                        "  <h3>Make:   <input type=\\\"text\\\" name=\"make\" value=\"" + make + "\" /> <h3>" +
                        "  <h3>Model:  <input type=\\\"text\\\" name=\"model\" value=\"" + model + "\" /> <h3>" +
                        "  <h3>Price:  <input type=\\\"text\\\" name=\"price\" value=\"" + price + "\" /> <h3>" +
                        "  <h3>Type:   <input type=\\\"text\\\" name=\"type\" value=\"" + type + "\" /> <h3>" +
                        "</form>\n" +
                        "</br></br>" +
                        "<input type=\"submit\" value=\"Save\"/>" +
                        "</div>");
                response.getWriter().println(html);
                response.setStatus(400);
            } else {
                System.out.println("ValidateFilter before run chain doFilter");
                filterChain.doFilter(servletRequest, response);
                System.out.println("ValidateFilter after run chain doFilter");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
