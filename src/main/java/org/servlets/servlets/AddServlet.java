package org.servlets.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servlets.dao.QuestionsDAO;
import org.servlets.model.Answer;
import org.servlets.model.Question;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(AddServlet.class);
    private static final int ANSWERS_COUNT = 3;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Edit Question</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            padding: 20px;\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            max-width: 1000px;\n" +
                "            margin: 0 auto;\n" +
                "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        h2 {\n" +
                "            text-align: center;\n" +
                "            color: #333;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .form-group {\n" +
                "            margin-bottom: 15px;\n" +
                "        }\n" +
                "\n" +
                "        .form-group label {\n" +
                "            display: block;\n" +
                "            font-weight: bold;\n" +
                "            margin-bottom: 5px;\n" +
                "        }\n" +
                "\n" +
                "        .form-group textarea,\n" +
                "        .form-group select {\n" +
                "            width: 100%;\n" +
                "            padding: 10px;\n" +
                "            border: 1px solid #ddd;\n" +
                "            border-radius: 5px;\n" +
                "            box-sizing: border-box;\n" +
                "            resize: vertical; /* Allow users to resize the height */\n" +
                "        }\n" +
                "\n" +
                "        .form-group textarea {\n" +
                "            min-height: 50px;\n" +
                "            max-height: 200px;\n" +
                "            line-height: 1.5;\n" +
                "            overflow: auto;\n" +
                "        }\n" +
                "\n" +
                "        .form-group select {\n" +
                "            padding: 9px;\n" +
                "        }\n" +
                "\n" +
                "        .form-group textarea:focus,\n" +
                "        .form-group select:focus {\n" +
                "            border-color: #007BFF;\n" +
                "            outline: none;\n" +
                "        }\n" +
                "\n" +
                "        .form-actions {\n" +
                "            display: flex;\n" +
                "            justify-content: space-between;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .button {\n" +
                "            padding: 10px 15px;\n" +
                "            border: none;\n" +
                "            border-radius: 5px;\n" +
                "            cursor: pointer;\n" +
                "            font-size: 16px;\n" +
                "            transition: background-color 0.3s;\n" +
                "        }\n" +
                "\n" +
                "        .button-save {\n" +
                "            background-color: #28a745;\n" +
                "            color: white;\n" +
                "        }\n" +
                "\n" +
                "        .button-cancel {\n" +
                "            background-color: #dc3545;\n" +
                "            color: white;\n" +
                "        }\n" +
                "\n" +
                "        .button-save:hover {\n" +
                "            background-color: #218838;\n" +
                "        }\n" +
                "\n" +
                "        .button-cancel:hover {\n" +
                "            background-color: #c82333;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n");

        html.append("    <div class=\"container\">\n" +
                "        <h2>Add Question</h2>\n" +
                "        <form action=\"\" method=\"POST\">\n" +
                "            <div class=\"form-group\">\n" +
                "                <label for=\"question\">Question:</label>\n" +
                "                <textarea id=\"question\" name=\"question\" placeholder=\"Enter text for Question ...\" required></textarea>\n" +
                "            </div>\n" +
                "\n");
        for (int i = 1; i <= ANSWERS_COUNT; i++) {
            html.append("        <div class=\"form-group\">\n" +
                    "                <label for=\"answer" + i + "\">Answer " + i + ":</label>\n" +
                    "                <textarea id=\"answer" + i + "\" name=\"answer" + i + "\" placeholder=\"Enter text for Answer ...\" required></textarea>\n" +
                    "                <label for=\"isTrue" + i + "\">Is Correct:</label>\n" +
                    "                <select id=\"isTrue" + i + "\" name=\"isTrue" + i + "\" required>\n" +
                    "                    <option value=\"true\" selected>True</option>\n" +
                    "                    <option value=\"false\">False</option>\n" +
                    "                </select>\n" +
                    "            </div>\n" +
                    "\n");
        }

        html.append("         <div class=\"form-actions\">\n" +
                "                <button type=\"submit\" class=\"button button-save\">Save</button>\n" +
                "                <button type=\"button\" class=\"button button-cancel\" onclick=\"window.location.href='http://localhost:8080/servlets-quiz/questions'\">Cancel</button>\n" +
                "            </div>\n");


        html.append("        </form>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n");
        PrintWriter writer = resp.getWriter();
        writer.println(html);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            logger.info("Start adding question");
            List<Answer> answers = new ArrayList<>();
            for (int i = 1; i <= ANSWERS_COUNT; i++) {
                String answerId = UUID.randomUUID().toString();
                String answerName = req.getParameter("answer" + i);
                boolean answerIsCorrect = Boolean.parseBoolean(req.getParameter("isTrue" + i));
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
        } catch (Exception e) {
            logger.error("Exception occurred adding new question", e);
            throw new RuntimeException(e);
        }
        logger.info("End adding question");
    }
}
