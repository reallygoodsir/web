package org.servlets.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servlets.dao.UsersDAO;

import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(AdminServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            logger.info("No session. Return login page.");
            StringBuilder html = new StringBuilder();
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
                    "        .error-message {\n" +
                    "            color: #d8000c;\n" +
                    "            background-color: #ffd2d2;\n" +
                    "            padding: 10px;\n" +
                    "            border-radius: 5px;\n" +
                    "            margin-bottom: 20px;\n" +
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
                    "<body>\n");
            html.append("    <div class=\"login-container\">\n");

            String errorCredentials = (String) req.getAttribute("errorCredentials");
            System.out.println("ATTRIBUTE:" + errorCredentials);
            if(errorCredentials != null && !errorCredentials.isEmpty()) {
                if (errorCredentials.equalsIgnoreCase("errorCredentials")) {
                    System.out.println("entered the append");
                    html.append("        <div class=\"error-message\" id=\"error-message\">\n" +
                            "            You entered an incorrect username or password. Please try again.\n" +
                            "        </div>\n");
                }
            }

//            String header = req.getHeader("errorMessage");
//            System.out.println("\n\n\n\n\n\n" + header + "\n\n\n\n\n\n");
//            if("incorrect credentials".equalsIgnoreCase(header)) {
//                html.append("        <div class=\"error-message\" id=\"error-message\" style=\"display: none;\">\n" +
//                        "            You entered an incorrect username or password. Please try again.\n" +
//                        "        </div>\n");
//            }
            html.append("        <h2>Login</h2>\n" +
                    "        <form method=\"POST\">\n" +
                    "            <input type=\"text\" id=\"username\" name=\"username\" placeholder=\"Enter your username\" required>\n" +
                    "            <input type=\"password\" id=\"password\" name=\"password\" placeholder=\"Enter your password\" required>\n" +
                    "            <button type=\"submit\">Login</button>\n" +
                    "        </form>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>\n");
            PrintWriter writer = resp.getWriter();
            writer.println(html);
            writer.flush();
            writer.close();
        } else {
            logger.info("Session exists. Navigate to questions page.");
            resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userName = req.getParameter("username");
        String password = req.getParameter("password");
        UsersDAO usersDAO = new UsersDAO();
        boolean isValid = usersDAO.validate(userName, password);
        if (isValid) {
            HttpSession session = req.getSession(true);
            if (session == null) {
                logger.info("Could not create session");
            } else {
                logger.info("Session created with id {}", session.getId());
                resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
            }
        } else {
            req.setAttribute("errorCredentials", "errorCredentials");
            doGet(req, resp);
        }
    }
}
