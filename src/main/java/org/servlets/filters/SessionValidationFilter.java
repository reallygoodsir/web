package org.servlets.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionValidationFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(SessionValidationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try {
            logger.info("Start session validation filter");
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpSession session = request.getSession(false);
            if (session == null) {
                logger.info("No session. Before redirect to admin page");
                ((HttpServletResponse) servletResponse).sendRedirect("http://localhost:8080/servlets-quiz/admin");
                logger.info("No session. After redirect to admin page");
            } else {
                logger.info("Before filter chain");
                filterChain.doFilter(servletRequest, servletResponse);
                logger.info("After filter chain");
            }
        } catch (Exception exception) {
            logger.error("Exception occurred during session validation", exception);
            ((HttpServletResponse) servletResponse).setStatus(500);
        }
    }

    @Override
    public void destroy() {

    }
}
