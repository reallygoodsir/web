package org.servlets.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servlets.dao.QuestionsDAO;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class DeleteServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(DeleteServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doDelete(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String questionId = req.getParameter("id");
        if (questionId == null) {
            resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
        } else {
            QuestionsDAO questionsDAO = new QuestionsDAO();
            try {
                questionsDAO.deleteQuestion(questionId);
                resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
            } catch (SQLException e) {
                logger.error("Exception occurred deleting existing question", e);
                throw new RuntimeException(e);
            }
        }
    }
}
