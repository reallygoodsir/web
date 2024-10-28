package org.servlets.quiz.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servlets.quiz.dao.QuestionsDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteQuestionServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(DeleteQuestionServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDelete(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String questionId = req.getParameter("id");
            if (questionId == null) {
                resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
            } else {
                QuestionsDAO questionsDAO = new QuestionsDAO();
                questionsDAO.deleteQuestion(questionId);
                resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
            }
        } catch (Exception exception) {
            LOGGER.error("Error deleting the question.", exception);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/error");
            dispatcher.forward(req, resp);
        }
    }
}
