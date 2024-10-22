package org.servlets.servlets;

import org.servlets.dao.QuestionsDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class DeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDelete(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String questionId = req.getParameter("id");
        if (questionId == null) {
            resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
        } else {
            QuestionsDAO questionsDAO = new QuestionsDAO();
            try {
                questionsDAO.deleteQuestion(questionId);
                resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
