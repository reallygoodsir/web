package org.servlets.servlets;

import org.servlets.dao.QuestionsDAO;
import org.servlets.dao.UsersDAO;
import org.servlets.model.Answer;
import org.servlets.model.Question;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        StringBuilder html = new StringBuilder();
        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println("No session. Return login page.");
            html.append("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Login Page</title>\n" +
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
                    "        .login-container {\n" +
                    "            background: #fff;\n" +
                    "            padding: 20px 30px;\n" +
                    "            border-radius: 10px;\n" +
                    "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                    "            width: 100%;\n" +
                    "            max-width: 400px;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "\n" +
                    "        .login-container h2 {\n" +
                    "            margin-bottom: 20px;\n" +
                    "            color: #333;\n" +
                    "        }\n" +
                    "\n" +
                    "        .login-container input[type=\"text\"],\n" +
                    "        .login-container input[type=\"password\"] {\n" +
                    "            width: 100%;\n" +
                    "            padding: 10px;\n" +
                    "            margin: 10px 0;\n" +
                    "            border: 1px solid #ddd;\n" +
                    "            border-radius: 5px;\n" +
                    "            box-sizing: border-box;\n" +
                    "        }\n" +
                    "\n" +
                    "        .login-container button {\n" +
                    "            width: 100%;\n" +
                    "            padding: 10px;\n" +
                    "            background: #4CAF50;\n" +
                    "            color: #fff;\n" +
                    "            border: none;\n" +
                    "            border-radius: 5px;\n" +
                    "            font-size: 16px;\n" +
                    "            cursor: pointer;\n" +
                    "            transition: background 0.3s ease;\n" +
                    "        }\n" +
                    "\n" +
                    "        .login-container button:hover {\n" +
                    "            background: #45a049;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"login-container\">\n" +
                    "        <h2>Login</h2>\n" +
                    "        <form method=\"POST\">\n" +
                    "            <input type=\"text\" id=\"username\" name=\"username\" placeholder=\"Enter your username\" required>\n" +
                    "            <input type=\"password\" id=\"password\" name=\"password\" placeholder=\"Enter your password\" required>\n" +
                    "            <button type=\"submit\">Login</button>\n" +
                    "        </form>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>\n");
            writer.println(html);
        } else {
            System.out.println("Session exists");
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
                    "            max-width: 500px;\n" +
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
                    "        .form-container button {\n" +
                    "            width: 100%;\n" +
                    "            padding: 10px;\n" +
                    "            margin-top: 20px;\n" +
                    "            background: #4CAF50;\n" +
                    "            color: #fff;\n" +
                    "            border: none;\n" +
                    "            border-radius: 5px;\n" +
                    "            font-size: 16px;\n" +
                    "            cursor: pointer;\n" +
                    "            transition: background 0.3s ease;\n" +
                    "        }\n" +
                    "\n" +
                    "        .form-container button:hover {\n" +
                    "            background: #45a049;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"form-container\">\n" +
                    "        <h2>Quiz Admin Panel</h2>\n" +
                    "        <form method=\"POST\">\n" +
                    "            <label for=\"question\">Question</label>\n" +
                    "            <textarea id=\"question\" name=\"question\" rows=\"4\" placeholder=\"Enter text for Question ...\" required></textarea>\n" +
                    "\n" +
                    "            <label for=\"answerOne\">Answer One</label>\n" +
                    "            <textarea id=\"answerOne\" name=\"answerOne\" rows=\"4\" placeholder=\"Enter text for Answer ...\" required></textarea>\n" +
                    "            <select id=\"isTrueOne\" name=\"isTrueOne\" required>\n" +
                    "                <option value=\"true\">True</option>\n" +
                    "                <option value=\"false\">False</option>\n" +
                    "            </select>\n" +
                    "\n" +
                    "            <label for=\"answerTwo\">Answer Two</label>\n" +
                    "            <textarea id=\"answerTwo\" name=\"answerTwo\" rows=\"4\" placeholder=\"Enter text for Answer ...\" required></textarea>\n" +
                    "            <select id=\"isTrueTwo\" name=\"isTrueTwo\" required>\n" +
                    "                <option value=\"true\">True</option>\n" +
                    "                <option value=\"false\">False</option>\n" +
                    "            </select>\n" +
                    "\n" +
                    "            <label for=\"answerThree\">Answer Three</label>\n" +
                    "            <textarea id=\"answerThree\" name=\"answerThree\" rows=\"4\" placeholder=\"Enter text for Answer ...\" required></textarea>\n" +
                    "            <select id=\"isTrueThree\" name=\"isTrueThree\" required>\n" +
                    "                <option value=\"true\">True</option>\n" +
                    "                <option value=\"false\">False</option>\n" +
                    "            </select>\n" +
                    "\n" +
                    "            <button type=\"submit\">Add Question</button>\n" +
                    "        </form>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>\n");
            writer.println(html);
        }
        writer.flush();
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            try {
                Answer firstAnswer = new Answer(req.getParameter("answerOne"), req.getParameter("isTrueOne"));
                Answer secondAnswer = new Answer(req.getParameter("answerTwo"), req.getParameter("isTrueTwo"));
                Answer thirdAnswer = new Answer(req.getParameter("answerThree"), req.getParameter("isTrueThree"));

                List<Answer> answers = new ArrayList<>();
                answers.add(firstAnswer);
                answers.add(secondAnswer);
                answers.add(thirdAnswer);
                Question question = new Question(req.getParameter("question"), answers);

                QuestionsDAO questionsDAO = new QuestionsDAO();
                questionsDAO.saveQuestion(question);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            doGet(req, resp);
        } else {
            try {
                UsersDAO usersDAO = new UsersDAO();
                if (usersDAO.validate(req.getParameter("username"), req.getParameter("password"))) {
                    session = req.getSession(true);
                    System.out.println("Session created with id " + session.getId());
                    doGet(req, resp);
                } else {
                    doGet(req, resp);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
