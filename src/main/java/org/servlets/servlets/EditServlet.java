package org.servlets.servlets;

import org.servlets.dao.QuestionsDAO;
import org.servlets.model.Answer;
import org.servlets.model.Question;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String questionId = req.getParameter("id");
        HttpSession session = req.getSession(true);
        session.setAttribute("questionId", questionId);

        if (questionId == null) {
            resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
        } else {
            QuestionsDAO questionsDAO = new QuestionsDAO();
            Question question = questionsDAO.getQuestionById(questionId);

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
                    "            max-width: 600px;\n" +
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
                    "        <h2>Edit Question</h2>\n" +
                    "        <form action=\"\" method=\"POST\">\n" +
                    "            <div class=\"form-group\">\n" +
                    "                <label for=\"question\">Question:</label>\n" +
                    "                <textarea id=\"question\" name=\"question\" required>" + question.getName() + "</textarea>\n" +
                    "            </div>\n" +
                    "\n");
            List<Answer> answers = question.getAnswers();
            for (int i = 0; i < answers.size(); i++) {
                Answer answer = answers.get(i);
                boolean isCorrect = answer.getIsCorrect();
                int answerIndex = i + 1;
                html.append("            <div class=\"form-group\">\n" +
                        "                <label for=\"answer" + answerIndex + "\">Answer " + answerIndex + ":</label>\n" +
                        "                <textarea id=\"answer" + answerIndex + "\" name=\"answer" + answerIndex + "\" required>" + answer.getName() + "</textarea>\n" +
                        "                <label for=\"isCorrect" + answerIndex + "\">Is Correct:</label>\n" +
                        "                <select id=\"isCorrect" + answerIndex + "\" name=\"isCorrect" + answerIndex + "\" required>\n");
                if (isCorrect) {
                    html.append("                    <option value=\"true\" selected>True</option>\n");
                    html.append("                    <option value=\"false\">False</option>\n");
                } else {
                    html.append("                    <option value=\"true\">True</option>\n");
                    html.append("                    <option value=\"false\" selected>False</option>\n");
                }
                html.append("                </select>\n" +
                        "            </div>\n" +
                        "\n");
            }

            html.append("            <div class=\"form-actions\">\n" +
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
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String questionName = req.getParameter("question");
        HttpSession session = req.getSession(false);
        String questionId = (String) session.getAttribute("questionId");

        QuestionsDAO questionsDAO = new QuestionsDAO();
        Question oldQuestion = questionsDAO.getQuestionById(questionId);
        Question newQuestion = new Question(questionName);
        newQuestion.setName(questionName);
        List<Answer> answers = new ArrayList<>();
        for (int i = 1; i <= oldQuestion.getAnswers().size(); i++) {
            String answerName = req.getParameter("answer" + i);
            boolean answerIsCorrect = Boolean.parseBoolean(req.getParameter("isCorrect" + i));
            Answer answer = new Answer();
            answer.setName(answerName);
            answer.setIsCorrect(answerIsCorrect);
            answer.setQuestionId(newQuestion.getId());
            answers.add(answer);
        }
        newQuestion.setAnswers(answers);
        try {
            questionsDAO.editQuestion(oldQuestion, newQuestion);
            resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
