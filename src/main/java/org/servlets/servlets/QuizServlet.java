package org.servlets.servlets;

import com.mysql.cj.Session;
import org.servlets.dao.QuizDAO;
import org.servlets.dao.QuizDisplayDAO;
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

public class QuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        PrintWriter writer = resp.getWriter();
        QuizDAO quizDAO = new QuizDAO();
        try {
            List<Question> questions = quizDAO.getQuestions();

            String page = req.getParameter("page");
            if (page == null) {
                page = "1";
            }
            Integer integerPage = Integer.valueOf(page);
            Integer nextPage = integerPage + 1;
            Integer previousPage = integerPage - 1;
            session.setAttribute("integerPage", integerPage);
            if (questions == null) {
                writer.println("an error happened while getting questions");
            } else {

                if (session.isNew()) {
                    session.setAttribute("score", 0);
                    List<Integer> temp = new ArrayList<>(questions.size());
                    session.setAttribute("scoreList", temp);
                }
//                List<Integer> localScoreList = new ArrayList<>(questions.size());
//                if(!session.isNew()) {
//                    Object attribute = session.getAttribute("scoreList");
//                    List<Integer> attributeScoreList = (ArrayList<Integer>) attribute;
//                    Integer score = (Integer) session.getAttribute("score");
//                    if(score != null) {
//                        attributeScoreList.add(integerPage - 1, score);
//                        System.out.println("Attribute Score List: ");
//                        for (Integer i : attributeScoreList) {
//                            System.out.println(i);
//                        }
//                    }
//                    session.setAttribute("scoreList", attributeScoreList);
//                }
//                for(int i = 0; i < attributeScoreList.size(); i++){
//                    localScoreList.set(i, attributeScoreList.get(i));
//                }

                Object attribute = session.getAttribute("userAnswerStatus");
                String userAnswer = "";
                boolean userAnswerExists = true;
                if (attribute != null) {
                    userAnswer = (String) attribute;
                    System.out.println(userAnswer);
                } else {
                    userAnswerExists = false;
                }
                QuizDisplayDAO quizDisplayDAO = new QuizDisplayDAO();
                Question question = quizDisplayDAO.getRecords(integerPage, questions);
                List<Answer> answers = question.getAnswers();
                Answer answerOne = answers.get(0);
                Answer answerTwo = answers.get(1);
                Answer answerThree = answers.get(2);
                StringBuilder html = new StringBuilder();
                html.append("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Select an Option</title>\n" +
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
                        "            max-width: 400px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .form-container .static-label {\n" +
                        "            text-align: center;\n" +
                        "            font-size: 18px;\n" +
                        "            font-weight: bold;\n" +
                        "            color: #333;\n" +
                        "            background-color: #e8f5e9;\n" +
                        "            padding: 10px;\n" +
                        "            border-radius: 5px;\n" +
                        "            border: 2px solid #4CAF50;\n" +
                        "            margin-bottom: 20px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .form-container h2 {\n" +
                        "            text-align: center;\n" +
                        "            margin-bottom: 20px;\n" +
                        "            color: #333;\n" +
                        "        }\n" +
                        "\n" +
                        "        .form-container .option {\n" +
                        "            display: flex;\n" +
                        "            align-items: center;\n" +
                        "            background: #f9f9f9;\n" +
                        "            padding: 10px 15px;\n" +
                        "            margin-bottom: 10px;\n" +
                        "            border-radius: 5px;\n" +
                        "            cursor: pointer;\n" +
                        "            border: 2px solid transparent;\n" +
                        "            transition: border-color 0.3s ease;\n" +
                        "        }\n" +
                        "\n" +
                        "        .form-container .option:hover {\n" +
                        "            border-color: #4CAF50;\n" +
                        "        }\n" +
                        "\n" +
                        "        .form-container input[type=\"radio\"] {\n" +
                        "            display: none;\n" +
                        "        }\n" +
                        "\n" +
                        "        .form-container input[type=\"radio\"]:checked + .option {\n" +
                        "            border-color: #4CAF50;\n" +
                        "            background: #e8f5e9;\n" +
                        "        }\n" +
                        "\n" +
                        "        .form-container .option-text {\n" +
                        "            margin-left: 10px;\n" +
                        "            font-size: 16px;\n" +
                        "            color: #555;\n" +
                        "        }\n" +
                        "\n" +
                        "        .form-container .buttons {\n" +
                        "            display: flex;\n" +
                        "            justify-content: space-between;\n" +
                        "            margin-top: 20px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .form-container button {\n" +
                        "            background-color: #4CAF50;\n" +
                        "            color: #fff;\n" +
                        "            border: none;\n" +
                        "            padding: 10px 20px;\n" +
                        "            border-radius: 5px;\n" +
                        "            font-size: 16px;\n" +
                        "            cursor: pointer;\n" +
                        "            transition: background-color 0.3s ease;\n" +
                        "        }\n" +
                        "\n" +
                        "        .form-container button:hover {\n" +
                        "            background-color: #45a049;\n" +
                        "        }\n" +
                        "\n" +
                        "        .form-container button.secondary {\n" +
                        "            background-color: #008CBA;\n" +
                        "        }\n" +
                        "\n" +
                        "        .form-container button.secondary:hover {\n" +
                        "            background-color: #007bb5;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* New CSS for alignment */\n" +
                        "        .form-container .buttons > button {\n" +
                        "            flex: 1; /* Make buttons occupy equal space */\n" +
                        "            margin: 0 5px; /* Add margin between buttons */\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"form-container\">");
                if (userAnswerExists) {
                    html.append("<div class=\"static-label\">" + userAnswer + "</div>");
                }
                html.append("        <h2>" + question.getName() + "</h2>\n" +
                        "        <form method=\"POST\">\n" +
                        "            <input type=\"radio\" id=\"option1\" name=\"options\" value=\"" + answerOne.getIsCorrect() + "\">\n" +
                        "            <label class=\"option\" for=\"option1\">\n" +
                        "                <span class=\"option-text\">" + answerOne.getName() + "</span>\n" +
                        "            </label>\n" +
                        "\n" +
                        "            <input type=\"radio\" id=\"option2\" name=\"options\" value=\"" + answerTwo.getIsCorrect() + "\">\n" +
                        "            <label class=\"option\" for=\"option2\">\n" +
                        "                <span class=\"option-text\">" + answerTwo.getName() + "</span>\n" +
                        "            </label>\n" +
                        "\n" +
                        "            <input type=\"radio\" id=\"option3\" name=\"options\" value=\"" + answerThree.getIsCorrect() + "\">\n" +
                        "            <label class=\"option\" for=\"option3\">\n" +
                        "                <span class=\"option-text\">" + answerThree.getName() + "</span>\n" +
                        "            </label>\n" +
                        "\n" +
                        "            <div class=\"buttons\">\n");
                if (!(integerPage <= 1)) {
                    html.append("<button type=\"button\" class=\"secondary\" onclick=\"window.location.href='http://localhost:8080/servlets-quiz/quiz?page=" + previousPage + "'\">Previous</button>");
                }
                    html.append("                <button type=\"submit\">Submit</button>\n");
                if (!(integerPage >= questions.size())) {
                    html.append("                <button type=\"button\" class=\"secondary\" " +
                            "onclick=\"window.location.href='http://localhost:8080/servlets-quiz/quiz?page="
                            + nextPage + "'\">Next</button>\n");
                }else{
                    html.append("                <button type=\"button\" class=\"secondary\" " +
                            "onclick=\"window.location.href='http://localhost:8080/servlets-quiz/results'\">Final Score</button>\n");
                }
                html.append("            </div>" +
                        "        </form>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>\n");
                session.removeAttribute("userAnswerStatus");
                writer.println(html);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userAnswer = req.getParameter("options");
        if (userAnswer == null) {
            userAnswer = "false";
        }
        HttpSession session = req.getSession(false);
        if (userAnswer.equalsIgnoreCase("true")) {
            session.setAttribute("score", 1);
            session.setAttribute("userAnswerStatus", "Correct");
        } else {
            session.setAttribute("userAnswerStatus", "Incorrect");
            session.setAttribute("score", 0);
        }
        if(!session.isNew()) {
            Object attribute = session.getAttribute("scoreList");
            List<Integer> attributeScoreList = (ArrayList<Integer>) attribute;
            Integer score = (Integer) session.getAttribute("score");
            if(score != null) {
                Integer integerPage = (Integer) session.getAttribute("integerPage");
                System.out.println("attributeScoreList size " + attributeScoreList.size());
                System.out.println("integerPage - 1 " + (integerPage - 1));
                try {
                    if (attributeScoreList.get(integerPage - 1) != null) {
                        attributeScoreList.remove(integerPage - 1);
                    }
                }catch (Exception e){}
                attributeScoreList.add(integerPage - 1, score);
                System.out.println("Attribute Score List: ");
                for (Integer i : attributeScoreList) {
                    System.out.println(i);
                }
            }
            session.setAttribute("scoreList", attributeScoreList);
        }
        doGet(req, resp);
    }
}
