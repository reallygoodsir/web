package org.servlets.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servlets.dao.QuestionsDAO;
import org.servlets.model.Question;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class ShowQuestionsServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(ShowQuestionsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        QuestionsDAO questionsDAO = new QuestionsDAO();
        List<Question> questions;
        try {
            questions = questionsDAO.getQuestions();
        } catch (SQLException e) {
            logger.error("Error occurred getting questions from db.", e);
            throw new RuntimeException(e);
        }
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Questions</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            padding: 20px;\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "\n" +
                "        table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "            background-color: #ffffff;\n" +
                "            margin-bottom: 20px;\n" +
                "            table-layout: auto;\n" +
                "            word-wrap: break-word;\n" +
                "        }\n" +
                "\n" +
                "        th, td {\n" +
                "            padding: 12px;\n" +
                "            border: 1px solid #ddd;\n" +
                "            text-align: left;\n" +
                "            vertical-align: top;\n" +
                "        }\n" +
                "\n" +
                "        th {\n" +
                "            background-color: #007BFF;\n" +
                "            color: white;\n" +
                "        }\n" +
                "\n" +
                "        tr:nth-child(even) {\n" +
                "            background-color: #f2f2f2;\n" +
                "        }\n" +
                "\n" +
                "        .button {\n" +
                "            padding: 8px 12px;\n" +
                "            border: none;\n" +
                "            border-radius: 5px;\n" +
                "            cursor: pointer;\n" +
                "            font-size: 14px;\n" +
                "            background-color: #28a745; /* Unified button color */\n" +
                "            color: white;\n" +
                "            white-space: nowrap;\n" +
                "            transition: background-color 0.3s;\n" +
                "        }\n" +
                "\n" +
                "        .button:hover {\n" +
                "            background-color: #218838; /* Darker shade on hover */\n" +
                "        }\n" +
                "\n" +
                "        @media (max-width: 768px) {\n" +
                "            table {\n" +
                "                display: block;\n" +
                "                overflow-x: auto;\n" +
                "                white-space: nowrap;\n" +
                "            }\n" +
                "\n" +
                "            th, td {\n" +
                "                white-space: normal;\n" +
                "                padding: 8px;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h2>Questions</h2>\n" +
                "<button class=\"button button-edit\" onclick=\"window.location.href='http://localhost:8080/servlets-quiz/questions/add'\">Add Question</button>" +
                "    <table>\n" +
                "        <thead>\n" +
                "            <tr>\n" +
                "                <th>#</th>\n" +
                "                <th>Question</th>\n" +
                "                <th>Actions</th>\n" +
                "            </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n");
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            html.append("            <tr>\n" +
                    "                <td>" + (i + 1) + "</td>\n" +
                    "                <td>" + question.getName() + "</td>\n" +
                    "                <td>\n" +
                    "                    <button class=\"button\" onclick=\"window.location.href='http://localhost:8080/servlets-quiz/questions/view?id=" + question.getId() + "'\">View</button>\n" +
                    "                    <button class=\"button\" onclick=\"window.location.href='http://localhost:8080/servlets-quiz/questions/edit?id=" + question.getId() + "'\">Edit</button>\n" +
                    "                    <button class=\"button\" onclick=\"window.location.href='http://localhost:8080/servlets-quiz/questions/delete?id=" + question.getId() + "'\">Delete</button>" +
                    "                </td>\n" +
                    "            </tr>\n");
        }
        html.append("        </tbody>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>\n");

        PrintWriter writer = resp.getWriter();
        writer.println(html);
    }
}
