package org.servlets.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SessionValidationFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(SessionValidationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("Start session validation filter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.info("No session. Before redirect to admin page");
            ((HttpServletResponse) servletResponse).sendRedirect("http://localhost:8080/servlets-quiz/admin");
            logger.info("No session. After redirect to admin page");
            return;
        } else {
            logger.info("Before filter chain");
            filterChain.doFilter(servletRequest, servletResponse);
            logger.info("After filter chain");
        }
    }

    @Override
    public void destroy() {

    }
}
