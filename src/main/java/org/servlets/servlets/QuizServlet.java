package org.servlets.servlets;

import org.servlets.dao.QuestionsDAO;
import org.servlets.model.Answer;
import org.servlets.model.Question;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class QuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(true);
        try {
            if (session.isNew()) {
                QuestionsDAO questionsDAO = new QuestionsDAO();
                List<Question> questions = questionsDAO.getQuestions();
                session.setAttribute("questions", questions);

                session.setAttribute("score", 0);
                session.setAttribute("scoreList", new ArrayList<>(questions.size()));
            }
            List<Question> questions = (List<Question>) session.getAttribute("questions");

            String page = req.getParameter("page");
            if (page == null) {
                page = "1";
            }
            int currentPage = Integer.parseInt(page);
            int nextPage = currentPage + 1;
            int previousPage = currentPage - 1;
            session.setAttribute("currentPage", currentPage);

            Object attribute = session.getAttribute("userAnswerStatus");
            String userAnswer = "";
            boolean userAnswerExists = true;
            if (attribute != null) {
                userAnswer = (String) attribute;
                System.out.println(userAnswer);
            } else {
                userAnswerExists = false;
            }

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
                html.append("<div class=\"static-label\">").append(userAnswer).append("</div>");
            }

            Question question = questions.get(currentPage - 1);
            List<Answer> answers = question.getAnswers();
            html.append("<h2>").append(question.getName()).append("</h2>\n").append("<form method=\"POST\">\n");

            for (int i = 0; i < answers.size(); i++) {
                Answer answer = answers.get(i);
                html.append("<input type=\"radio\" id=\"option" + (i + 1) + "\" name=\"options\" value=\"" + question.getId() + "-" + answer.getId() + "\">\n" +
                        "<label class=\"option\" for=\"option" + (i + 1) + "\">\n" +
                        " <span class=\"option-text\">" + answer.getName() + "</span>\n" +
                        "</label>\n" +
                        "\n");
            }

            html.append(" <div class=\"buttons\">\n");

            if (!(currentPage <= 1)) {
                html.append("<button type=\"button\" class=\"secondary\" onclick=\"window.location.href='http://localhost:8080/servlets-quiz/quiz?page=" +
                        previousPage + "'\">Previous</button>");
            }

            html.append(" <button type=\"submit\">Submit</button>\n");
            if (!(currentPage >= questions.size())) {
                html.append(" <button type=\"button\" class=\"secondary\" " +
                        "onclick=\"window.location.href='http://localhost:8080/servlets-quiz/quiz?page="
                        + nextPage + "'\">Next</button>\n");
            } else {
                html.append("<button type=\"button\" class=\"secondary\" " +
                        "onclick=\"window.location.href='http://localhost:8080/servlets-quiz/results'\">Final Score</button>\n");
            }
            html.append("</div>" +
                    "        </form>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>\n");

            session.removeAttribute("userAnswerStatus");

            PrintWriter writer = resp.getWriter();
            writer.println(html);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String userAnswer = req.getParameter("options");
        System.out.println("userAnswer " + userAnswer);
        HttpSession session = req.getSession(false);
        String[] userAnswerArr = userAnswer.split("-");
        Integer userSelectedQuestionId = Integer.valueOf(userAnswerArr[0]);
        Integer userSelectedAnswerId = Integer.valueOf(userAnswerArr[1]);
        System.out.println("userSelectedQuestionId " + userSelectedQuestionId + " userSelectedAnswerId " + userSelectedAnswerId);
        boolean isUserAnswerCorrect = false;
        List<Question> questions = (List<Question>) session.getAttribute("questions");
        for (Question question : questions) {
            if (question.getId().equals(userSelectedQuestionId)) {
                List<Answer> answers = question.getAnswers();
                for (Answer answer : answers) {
                    if (answer.getId().equals(userSelectedAnswerId)) {
                        if ("true".equalsIgnoreCase(answer.getIsCorrect())) {
                            isUserAnswerCorrect = true;
                        }
                        break;
                    }
                }
                break;
            }
        }

        if (isUserAnswerCorrect) {
            session.setAttribute("score", 1);
            session.setAttribute("userAnswerStatus", "Correct");
        } else {
            session.setAttribute("userAnswerStatus", "Incorrect");
            session.setAttribute("score", 0);
        }
        if (!session.isNew()) {
            Object attribute = session.getAttribute("scoreList");
            List<Integer> attributeScoreList = (ArrayList<Integer>) attribute;
            Integer score = (Integer) session.getAttribute("score");
            if (score != null) {
                Integer currentPage = (Integer) session.getAttribute("currentPage");
                System.out.println("attributeScoreList size " + attributeScoreList.size());
                System.out.println("currentPage - 1 " + (currentPage - 1));
                try {
                    if (attributeScoreList.get(currentPage - 1) != null) {
                        attributeScoreList.remove(currentPage - 1);
                    }
                } catch (Exception e) {
                    System.out.println("Ignore exception " + e.getMessage());
                }
                attributeScoreList.add(currentPage - 1, score);
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
