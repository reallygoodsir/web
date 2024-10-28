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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddQuestionServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(AddQuestionServlet.class);
    private static final int ANSWERS_COUNT = 3;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
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
                    .append("        .form-group select {\n")
                    .append("            padding: 9px;\n")
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
                    .append("        <h2>Add Question</h2>\n")
                    .append("        <form action=\"\" method=\"POST\">\n")
                    .append("            <div class=\"form-group\">\n")
                    .append("                <label for=\"question\">Question:</label>\n")
                    .append("                <textarea id=\"question\" name=\"question\" placeholder=\"Enter text for Question ...\" required></textarea>\n")
                    .append("            </div>\n");

            for (int answerOrderNumber = 1; answerOrderNumber <= ANSWERS_COUNT; answerOrderNumber++) {
                html.append("        <div class=\"form-group\">\n")
                        .append("                <label for=\"answer" + answerOrderNumber + "\">Answer " + answerOrderNumber + ":</label>\n")
                        .append("                <textarea rows=\"8\" id=\"answer" + answerOrderNumber + "\" name=\"answer" + answerOrderNumber + "\" placeholder=\"Enter text for Answer ...\" required></textarea>\n")
                        .append("                <label for=\"isTrue" + answerOrderNumber + "\">Is Correct:</label>\n")
                        .append("                <select id=\"isTrue" + answerOrderNumber + "\" name=\"isTrue" + answerOrderNumber + "\" required>\n")
                        .append("                    <option value=\"true\" selected>True</option>\n")
                        .append("                    <option value=\"false\">False</option>\n")
                        .append("                </select>\n")
                        .append("            </div>\n");
            }

            html.append("         <div class=\"form-actions\">\n")
                    .append("                <button type=\"submit\" class=\"button button-save\">Save</button>\n")
                    .append("                <button type=\"button\" class=\"button button-cancel\" onclick=\"window.location.href='http://localhost:8080/servlets-quiz/questions'\">Cancel</button>\n")
                    .append("            </div>\n")
                    .append("        </form>\n")
                    .append("    </div>\n")
                    .append("</body>\n")
                    .append("</html>\n");

            PrintWriter writer = resp.getWriter();
            writer.println(html);
        } catch (Exception exception) {
            LOGGER.error("Error displaying add question page.", exception);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/error");
            dispatcher.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            LOGGER.info("Start adding question");
            List<Answer> answers = new ArrayList<>();
            for (int answerOrderNumber = 1; answerOrderNumber <= ANSWERS_COUNT; answerOrderNumber++) {
                String answerId = UUID.randomUUID().toString();
                String answerName = req.getParameter("answer" + answerOrderNumber);
                boolean answerIsCorrect = Boolean.parseBoolean(req.getParameter("isTrue" + answerOrderNumber));
                Answer answer = new Answer(answerId, answerName, answerIsCorrect);
                answers.add(answer);
            }

            String questionId = UUID.randomUUID().toString();
            String questionName = req.getParameter("question");
            Question question = new Question(questionId, questionName);
            question.setAnswers(answers);

            QuestionsDAO questionsDAO = new QuestionsDAO();
            questionsDAO.saveQuestion(question);

            resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
            LOGGER.info("End adding question");
        } catch (Exception exception) {
            LOGGER.error("Error adding new question.", exception);
            RequestDispatcher dispatcher = req

                    .getRequestDispatcher("/error");
            dispatcher.forward(req, resp);
        }
    }
}