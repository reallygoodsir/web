package org.servlets.quiz.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servlets.quiz.dao.UsersDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(AdminServlet.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String userName = req.getParameter("username");
            String password = req.getParameter("password");
            UsersDAO usersDAO = new UsersDAO();
            HttpSession sessionTest = req.getSession(false);
            if(sessionTest != null){
                sessionTest.invalidate();
            }
            boolean isValid = usersDAO.validate(userName, password);
            if (isValid) {
                HttpSession session = req.getSession(true);
                if (session == null) {
                    LOGGER.info("Could not create session");
                } else {
                    LOGGER.info("Session created with id {}", session.getId());
                    resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
                }
            } else {
                LOGGER.info("User validation: false");
                req.setAttribute("errorCredentials", "errorCredentials");
                RequestDispatcher dispatcher = req.getRequestDispatcher("/admin.jsp");
                dispatcher.forward(req, resp);
            }
        } catch (Exception exception) {
            LOGGER.error("Error validating credentials.", exception);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/error");
            dispatcher.forward(req, resp);
        }
    }
}
