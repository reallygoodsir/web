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
                "    <title>Quiz Admin Panel</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            background: #f0f2f5;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            height: 100vh;\n" +
                "            font-family: 'Arial', sans-serif;\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "\n" +
                "        .form-container {\n" +
                "            background: #fff;\n" +
                "            padding: 20px 30px;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                "            width: 100%;\n" +
                "            max-width: 1000px;\n" +
                "        }\n" +
                "\n" +
                "        .form-container h2 {\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 20px;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "\n" +
                "        .form-container label {\n" +
                "            display: block;\n" +
                "            margin-top: 15px;\n" +
                "            margin-bottom: 5px;\n" +
                "            font-weight: bold;\n" +
                "            color: #555;\n" +
                "        }\n" +
                "\n" +
                "        .form-container textarea {\n" +
                "            width: 100%;\n" +
                "            padding: 10px;\n" +
                "            border: 1px solid #ddd;\n" +
                "            border-radius: 5px;\n" +
                "            box-sizing: border-box;\n" +
                "            resize: vertical;\n" +
                "        }\n" +
                "\n" +
                "        .form-container select {\n" +
                "            width: 100%;\n" +
                "            padding: 10px;\n" +
                "            margin-top: 10px;\n" +
                "            border: 1px solid #ddd;\n" +
                "            border-radius: 5px;\n" +
                "            box-sizing: border-box;\n" +
                "            background: #fff;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "\n" +
                "        .button-container {\n" +
                "            display: flex;\n" +
                "            justify-content: space-between; /* Align buttons to opposite sides */\n" +
                "            margin-top: 20px; /* Adds some space above the buttons */\n" +
                "        }\n" +
                "\n" +
                "        .button-container button {\n" +
                "            width: 48%; /* Set a width for both buttons to make them the same size */\n" +
                "            padding: 10px; /* Ensure both buttons have the same padding */\n" +
                "            border: none; /* Remove border */\n" +
                "            border-radius: 5px; /* Match border radius */\n" +
                "            font-size: 16px; /* Font size */\n" +
                "            cursor: pointer; /* Change cursor to pointer */\n" +
                "            transition: background 0.3s ease; /* Transition for hover effect */\n" +
                "        }\n" +
                "\n" +
                "        .button-add {\n" +
                "            background: #4CAF50; /* Green background for the Add button */\n" +
                "            color: #fff; /* White text color for contrast */\n" +
                "        }\n" +
                "\n" +
                "        .button-add:hover {\n" +
                "            background: #45a049; /* Darker green on hover for Add button */\n" +
                "        }\n" +
                "\n" +
                "        .button-return {\n" +
                "            background: #dc3545; /* Set the background color to red for Return button */\n" +
                "            color: #fff; /* Set the text color to white for contrast */\n" +
                "        }\n" +
                "\n" +
                "        .button-return:hover {\n" +
                "            background: #c82333; /* Darker red on hover */\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"form-container\">\n" +
                "        <h2>Add Question</h2>\n" +
                "        <form method=\"POST\">\n" +
                "            <label for=\"question\">Question</label>\n" +
                "            <textarea id=\"question\" name=\"question\" rows=\"4\" placeholder=\"Enter text for Question ...\" required></textarea>\n" +
                "\n");

        for (int i = 1; i <= ANSWERS_COUNT; i++) {
            html.append("            <label for=\"answer" + i + "\">Answer " + i + "</label>\n" +
                    "            <textarea id=\"answer" + i + "\" name=\"answer" + i + "\" rows=\"4\" placeholder=\"Enter text for Answer ...\" required></textarea>\n" +
                    "            <select id=\"isTrue" + i + "\" name=\"isTrue" + i + "\" required>\n" +
                    "                <option value=\"true\">True</option>\n" +
                    "                <option value=\"false\">False</option>\n" +
                    "            </select>\n");

        }

        html.append("\n" +
                "<div class=\"button-container\">\n" +
                "            <button class=\"button-add\" type=\"submit\">Add</button>\n" +
                "<button class=\"button button-return\" type=\"button\" onclick=\"window.location.href='http://localhost:8080/servlets-quiz/questions'\">Return</button>" +
                "</div>\n" +
                "        </form>\n" +
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
