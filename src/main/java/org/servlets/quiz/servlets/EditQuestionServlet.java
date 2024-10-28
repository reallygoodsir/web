package org.servlets.quiz.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servlets.quiz.dao.QuestionsDAO;
import org.servlets.quiz.model.Answer;
import org.servlets.quiz.model.Question;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class EditQuestionServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(EditQuestionServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String questionId = req.getParameter("id");
            HttpSession session = req.getSession(true);
            session.setAttribute("questionId", questionId);

            if (questionId == null) {
                resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
            } else {
                QuestionsDAO questionsDAO = new QuestionsDAO();
                Question question = questionsDAO.getQuestionById(questionId);

                StringBuilder html = new StringBuilder();
                html.append("<!DOCTYPE html>\n")
                        .append("<html lang=\"en\">\n")
                        .append("<head>\n")
                        .append("    <meta charset=\"UTF-8\">\n")
                        .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
                        .append("    <title>Edit Question</title>\n")
                        .append("    <style>\n")
                        .append("        body {\n")
                        .append("            font-family: Arial, sans-serif;\n")
                        .append("            background-color: #f4f4f4;\n")
                        .append("            padding: 20px;\n")
                        .append("            box-sizing: border-box;\n")
                        .append("        }\n")
                        .append("        .container {\n")
                        .append("            background-color: #ffffff;\n")
                        .append("            padding: 20px;\n")
                        .append("            border-radius: 8px;\n")
                        .append("            max-width: 1000px;\n")
                        .append("            margin: 0 auto;\n")
                        .append("            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n")
                        .append("        }\n")
                        .append("        h2 {\n")
                        .append("            text-align: center;\n")
                        .append("            color: #333;\n")
                        .append("            margin-bottom: 20px;\n")
                        .append("        }\n")
                        .append("        .form-group {\n")
                        .append("            margin-bottom: 15px;\n")
                        .append("        }\n")
                        .append("        .form-group label {\n")
                        .append("            display: block;\n")
                        .append("            font-weight: bold;\n")
                        .append("            margin-bottom: 5px;\n")
                        .append("        }\n")
                        .append("        .form-group textarea,\n")
                        .append("        .form-group select {\n")
                        .append("            width: 100%;\n")
                        .append("            padding: 10px;\n")
                        .append("            border: 1px solid #ddd;\n")
                        .append("            border-radius: 5px;\n")
                        .append("            box-sizing: border-box;\n")
                        .append("            resize: vertical;\n")
                        .append("        }\n")
                        .append("        .form-group textarea {\n")
                        .append("            min-height: 50px;\n")
                        .append("            max-height: 200px;\n")
                        .append("            line-height: 1.5;\n")
                        .append("            overflow: auto;\n")
                        .append("        }\n")
                        .append("        .form-group textarea:focus,\n")
                        .append("        .form-group select:focus {\n")
                        .append("            border-color: #007BFF;\n")
                        .append("            outline: none;\n")
                        .append("        }\n")
                        .append("        .form-actions {\n")
                        .append("            display: flex;\n")
                        .append("            justify-content: space-between;\n")
                        .append("            margin-top: 20px;\n")
                        .append("        }\n")
                        .append("        .button {\n")
                        .append("            padding: 10px 15px;\n")
                        .append("            border: none;\n")
                        .append("            border-radius: 5px;\n")
                        .append("            cursor: pointer;\n")
                        .append("            font-size: 16px;\n")
                        .append("            transition: background-color 0.3s;\n")
                        .append("        }\n")
                        .append("        .button-save {\n")
                        .append("            background-color: #28a745;\n")
                        .append("            color: white;\n")
                        .append("        }\n")
                        .append("        .button-cancel {\n")
                        .append("            background-color: #dc3545;\n")
                        .append("            color: white;\n")
                        .append("        }\n")
                        .append("        .button-save:hover {\n")
                        .append("            background-color: #218838;\n")
                        .append("        }\n")
                        .append("        .button-cancel:hover {\n")
                        .append("            background-color: #c82333;\n")
                        .append("        }\n")
                        .append("    </style>\n")
                        .append("</head>\n")
                        .append("<body>\n")
                        .append("    <div class=\"container\">\n")
                        .append("        <h2>Edit Question</h2>\n")
                        .append("        <form action=\"\" method=\"POST\">\n")
                        .append("            <div class=\"form-group\">\n")
                        .append("                <label for=\"question\">Question:</label>\n")
                        .append("                <textarea id=\"question\" name=\"question\" required>")
                        .append(question.getName())
                        .append("</textarea>\n")
                        .append("            </div>\n");

                List<Answer> answers = question.getAnswers();
                for (int answerOrderNumber = 0; answerOrderNumber < answers.size(); answerOrderNumber++) {
                    Answer answer = answers.get(answerOrderNumber);
                    boolean isCorrect = answer.getIsCorrect();
                    int answerIndex = answerOrderNumber + 1;

                    html.append("            <div class=\"form-group\">\n")
                            .append("                <label for=\"answer-")
                            .append(answer.getId())
                            .append("\">Answer ")
                            .append(answerIndex)
                            .append(":</label>\n")
                            .append("                <textarea rows=\"8\" id=\"answer-")
                            .append(answer.getId())
                            .append("\" name=\"answer-")
                            .append(answer.getId())
                            .append("\" required>")
                            .append(answer.getName())
                            .append("</textarea>\n")
                            .append("                <label for=\"isCorrect-")
                            .append(answer.getId())
                            .append("\">Is Correct:</label>\n")
                            .append("                <select id=\"isCorrect-")
                            .append(answer.getId())
                            .append("\" name=\"isCorrect-")
                            .append(answer.getId())
                            .append("\" required>\n");

                    if (isCorrect) {
                        html.append("                    <option value=\"true\" selected>True</option>\n")
                                .append("                    <option value=\"false\">False</option>\n");
                    } else {
                        html.append("                    <option value=\"true\">True</option>\n")
                                .append("                    <option value=\"false\" selected>False</option>\n");
                    }

                    html.append("                </select>\n")
                            .append("            </div>\n");
                }

                html.append("            <div class=\"form-actions\">\n")
                        .append("                <button type=\"submit\" class=\"button button-save\">Save</button>\n")
                        .append("                <button type=\"button\" class=\"button button-cancel\" onclick=\"window.location.href='http://localhost:8080/servlets-quiz/questions'\">Cancel</button>\n")
                        .append("            </div>\n")
                        .append("        </form>\n")
                        .append("    </div>\n")
                        .append("</body>\n")
                        .append("</html>\n");

                PrintWriter writer = resp.getWriter();
                writer.println(html);
            }
        } catch (Exception exception) {
            LOGGER.error("Error displaying edit question page.", exception);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/error");
            dispatcher.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String questionName = req.getParameter("question");
            HttpSession session = req.getSession(false);
            String questionId = (String) session.getAttribute("questionId");

            QuestionsDAO questionsDAO = new QuestionsDAO();
            Question question = questionsDAO.getQuestionById(questionId);
            question.setName(questionName);
            List<Answer> answers = question.getAnswers();

            for (int answerOrderNumber = 1; answerOrderNumber <= answers.size(); answerOrderNumber++) {
                Answer answer = answers.get(answerOrderNumber - 1);
                String answerName = req.getParameter("answer-" + answer.getId());
                boolean isCorrect = Boolean.parseBoolean(req.getParameter("isCorrect-" + answer.getId()));
                answer.setName(answerName);
                answer.setIsCorrect(isCorrect);
            }

            questionsDAO.editQuestion(question);
            resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
        } catch (Exception exception) {
            LOGGER.error("Error editing the question.", exception);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/error");
            dispatcher.forward(req, resp);
        }
    }
}
