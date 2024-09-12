package org.cars.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getMethod().equalsIgnoreCase("GET")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            System.out.println("LogFilter before run chain doFilter");
            System.out.println("User entered id " + servletRequest.getParameter("id"));
            System.out.println("User entered type " + servletRequest.getParameter("type"));
            System.out.println("User entered price " + servletRequest.getParameter("price"));
            System.out.println("User entered name " + servletRequest.getParameter("name"));
            System.out.println("User entered model " + servletRequest.getParameter("model"));
            System.out.println("User entered make " + servletRequest.getParameter("make"));
            filterChain.doFilter(servletRequest, servletResponse);
            System.out.println("LogFilter after run chain doFilter");
        }
    }

    @Override
    public void destroy() {

    }
}
