package org.servlets.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

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
        }
        writer.flush();
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
    }
}
