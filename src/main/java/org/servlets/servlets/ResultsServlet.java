package org.servlets.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ResultsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if(session == null){
            resp.setStatus(500);

        }else{
            Object attribute = session.getAttribute("scoreList");
            List<Integer> attributeScoreList = (ArrayList<Integer>) attribute;
            Integer finalScore = 0;
            for (Integer i : attributeScoreList) {
                finalScore += i;
            }
            PrintWriter writer = resp.getWriter();
            writer.println("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Quiz Results</title>\n" +
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
                    "        .container {\n" +
                    "            background: #fff;\n" +
                    "            padding: 20px 30px;\n" +
                    "            border-radius: 10px;\n" +
                    "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                    "            text-align: center;\n" +
                    "            width: 100%;\n" +
                    "            max-width: 400px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .static-label {\n" +
                    "            font-size: 24px;\n" +
                    "            font-weight: bold;\n" +
                    "            color: #333;\n" +
                    "            background-color: #e8f5e9;\n" +
                    "            padding: 10px;\n" +
                    "            border-radius: 5px;\n" +
                    "            border: 2px solid #4CAF50;\n" +
                    "            margin: 20px 0;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <div class=\"static-label\">Your score is " + finalScore + "/" + attributeScoreList.size() + "</div>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
