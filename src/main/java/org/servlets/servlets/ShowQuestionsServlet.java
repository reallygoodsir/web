package org.servlets.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servlets.dao.QuestionsDAO;
import org.servlets.model.Question;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowQuestionsServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(ShowQuestionsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String page = req.getParameter("page");
            if (page == null) {
                page = "1";
            }
            int currentPage = Integer.parseInt(page);
            int nextPage = currentPage + 1;
            int previousPage = currentPage - 1;

            int beginning = 0; // change that name
            for (int i = 1; i < currentPage; i++) {
                beginning += 8; // 8 questions a page
            }
            QuestionsDAO questionsDAO = new QuestionsDAO();
            List<Question> questions = new ArrayList<>();
            List<Question> temporaryQuestions;
            try {
                temporaryQuestions = questionsDAO.getQuestions();
            } catch (SQLException e) {
                logger.error("Error occurred getting questions from db.", e);
                throw new RuntimeException(e);
            }
            if (temporaryQuestions.isEmpty()) {
                PrintWriter writer = resp.getWriter();
                writer.println("<!DOCTYPE html>\n" +
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
                        "        .pagination-container {\n" +
                        "            margin-top: 20px; /* Space between the table and pagination */\n" +
                        "            display: flex; /* Use flexbox for alignment */\n" +
                        "            justify-content: center; /* Center the buttons */\n" +
                        "        }\n" +
                        "\n" +
                        "        .pagination-button {\n" +
                        "            padding: 10px 16px; /* More padding for height */\n" +
                        "            border: none;\n" +
                        "            border-radius: 5px;\n" +
                        "            cursor: pointer;\n" +
                        "            font-size: 14px;\n" +
                        "            background-color: #007BFF; /* Blue color for pagination buttons */\n" +
                        "            color: white;\n" +
                        "            white-space: nowrap;\n" +
                        "            margin: 0 5px; /* Space between buttons */\n" +
                        "            transition: background-color 0.3s;\n" +
                        "        }\n" +
                        "\n" +
                        "        .pagination-button:hover {\n" +
                        "            background-color: #0056b3; /* Darker shade on hover */\n" +
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
                        "</head>" +
                        "<button class=\"button button-edit\" onclick=\"window.location.href='http://localhost:8080/servlets-quiz/questions/add'\">Add Question</button>");
            } else {
                int amountToAdd = 8; // change the name
                if (beginning == 0) {
                    amountToAdd -= 1;
                }
                for (int i = beginning; i <= beginning + amountToAdd && i < temporaryQuestions.size(); i++) {
                    Question question = temporaryQuestions.get(i);
                    if (question == null) {
                        break;
                    }
                    questions.add(question);
                }
                if (questions.size() == 0) {
                    resp.sendRedirect("http://localhost:8080/servlets-quiz/questions?page=" + previousPage);
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
                        "        .pagination-container {\n" +
                        "            margin-top: 20px; /* Space between the table and pagination */\n" +
                        "            display: flex; /* Use flexbox for alignment */\n" +
                        "            justify-content: center; /* Center the buttons */\n" +
                        "        }\n" +
                        "\n" +
                        "        .pagination-button {\n" +
                        "            padding: 10px 16px; /* More padding for height */\n" +
                        "            border: none;\n" +
                        "            border-radius: 5px;\n" +
                        "            cursor: pointer;\n" +
                        "            font-size: 14px;\n" +
                        "            background-color: #007BFF; /* Blue color for pagination buttons */\n" +
                        "            color: white;\n" +
                        "            white-space: nowrap;\n" +
                        "            margin: 0 5px; /* Space between buttons */\n" +
                        "            transition: background-color 0.3s;\n" +
                        "        }\n" +
                        "\n" +
                        "        .pagination-button:hover {\n" +
                        "            background-color: #0056b3; /* Darker shade on hover */\n" +
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
                        "</head>" +
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
                for (int i = 0, i2 = beginning; i < questions.size(); i++, i2++) {
                    Question question = questions.get(i);
                    html.append("            <tr>\n" +
                            "                <td>" + (i2 + 1) + "</td>\n" +
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
                        "    <div class=\"pagination-container\">\n");
                if (currentPage != 1) {
                    html.append("        <button class=\"pagination-button\" onclick=\"window.location.href='http://localhost:8080/servlets-quiz/questions?page=" + previousPage + "'\">Previous</button>\n");
                }
                html.append("        <button class=\"pagination-button\" onclick=\"window.location.href='http://localhost:8080/servlets-quiz/questions?page=" + nextPage + "'\">Next</button>\n");
                html.append("    </div>\n" +
                        "</body>\n" +
                        "</html>\n");

                PrintWriter writer = resp.getWriter();
                writer.println(html);
            }
        } catch (Exception exception) {
            logger.error("Error displaying the questions.", exception);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/error");
            dispatcher.forward(req, resp);
        }
    }
}
