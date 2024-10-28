package org.servlets.quiz.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SessionValidationFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(SessionValidationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            LOGGER.info("Start session validation filter");
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpSession session = request.getSession(false);
            if (session == null) {
                LOGGER.info("No session. Before redirect to admin page");
                ((HttpServletResponse) servletResponse).sendRedirect("http://localhost:8080/servlets-quiz/admin");
                LOGGER.info("No session. After redirect to admin page");
            } else {
                LOGGER.info("Before filter chain");
                filterChain.doFilter(servletRequest, servletResponse);
                LOGGER.info("After filter chain");
            }
        } catch (Exception exception) {
            LOGGER.error("Exception occurred during session validation", exception);
            RequestDispatcher dispatcher = servletRequest.getRequestDispatcher("/error");
            dispatcher.forward(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
