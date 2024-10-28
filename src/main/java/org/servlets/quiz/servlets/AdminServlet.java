package org.servlets.quiz.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servlets.quiz.dao.UsersDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(AdminServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            if (session == null) {
                LOGGER.info("No session. Return login page.");
                StringBuilder html = new StringBuilder();
                html.append("<!DOCTYPE html>\n")
                        .append("<html lang=\"en\">\n")
                        .append("<head>\n")
                        .append("    <meta charset=\"UTF-8\">\n")
                        .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
                        .append("    <title>Login Page</title>\n")
                        .append("    <style>\n")
                        .append("        body {\n")
                        .append("            background: #f0f2f5;\n")
                        .append("            display: flex;\n")
                        .append("            justify-content: center;\n")
                        .append("            align-items: center;\n")
                        .append("            height: 100vh;\n")
                        .append("            font-family: 'Arial', sans-serif;\n")
                        .append("            margin: 0;\n")
                        .append("        }\n")
                        .append("        .login-container {\n")
                        .append("            background: #fff;\n")
                        .append("            padding: 20px 30px;\n")
                        .append("            border-radius: 10px;\n")
                        .append("            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n")
                        .append("            width: 100%;\n")
                        .append("            max-width: 400px;\n")
                        .append("            text-align: center;\n")
                        .append("        }\n")
                        .append("        .login-container h2 {\n")
                        .append("            margin-bottom: 20px;\n")
                        .append("            color: #333;\n")
                        .append("        }\n")
                        .append("        .error-message {\n")
                        .append("            color: #d8000c;\n")
                        .append("            background-color: #ffd2d2;\n")
                        .append("            padding: 10px;\n")
                        .append("            border-radius: 5px;\n")
                        .append("            margin-bottom: 20px;\n")
                        .append("        }\n")
                        .append("        .login-container input[type=\"text\"],\n")
                        .append("        .login-container input[type=\"password\"] {\n")
                        .append("            width: 100%;\n")
                        .append("            padding: 10px;\n")
                        .append("            margin: 10px 0;\n")
                        .append("            border: 1px solid #ddd;\n")
                        .append("            border-radius: 5px;\n")
                        .append("            box-sizing: border-box;\n")
                        .append("        }\n")
                        .append("        .login-container button {\n")
                        .append("            width: 100%;\n")
                        .append("            padding: 10px;\n")
                        .append("            background: #4CAF50;\n")
                        .append("            color: #fff;\n")
                        .append("            border: none;\n")
                        .append("            border-radius: 5px;\n")
                        .append("            font-size: 16px;\n")
                        .append("            cursor: pointer;\n")
                        .append("            transition: background 0.3s ease;\n")
                        .append("        }\n")
                        .append("        .login-container button:hover {\n")
                        .append("            background: #45a049;\n")
                        .append("        }\n")
                        .append("    </style>\n")
                        .append("</head>\n")
                        .append("<body>\n")
                        .append("    <div class=\"login-container\">\n");

                String errorCredentials = (String) req.getAttribute("errorCredentials");
                if (errorCredentials != null && !errorCredentials.isEmpty()) {
                    if (errorCredentials.equalsIgnoreCase("errorCredentials")) {
                        html.append("        <div class=\"error-message\" id=\"error-message\">\n")
                                .append("            You entered an incorrect username or password. Please try again.\n")
                                .append("        </div>\n");
                    }
                }

                html.append("        <h2>Login</h2>\n")
                        .append("        <form method=\"POST\">\n")
                        .append("            <input type=\"text\" id=\"username\" name=\"username\" placeholder=\"Enter your username\" required>\n")
                        .append("            <input type=\"password\" id=\"password\" name=\"password\" placeholder=\"Enter your password\" required>\n")
                        .append("            <button type=\"submit\">Login</button>\n")
                        .append("        </form>\n")
                        .append("    </div>\n")
                        .append("</body>\n")
                        .append("</html>\n");

                PrintWriter writer = resp.getWriter();
                writer.println(html);
                writer.flush();
                writer.close();
            } else {
                LOGGER.info("Session exists. Navigate to questions page.");
                resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
            }
        } catch (Exception exception) {
            LOGGER.error("Error to show admin login page.", exception);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/error");
            dispatcher.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String userName = req.getParameter("username");
            String password = req.getParameter("password");
            UsersDAO usersDAO = new UsersDAO();
            boolean isValid = usersDAO.validate(userName, password);

            if (isValid) {
                HttpSession session = req.getSession(true);
                if (session == null) {
                    LOGGER.info("Could not create session");
                } else {
                    LOGGER.info("Session created with id {}", session.getId());
                    resp.sendRedirect("http://localhost:8080/servlets-quiz/questions");
                }
            } else {
                req.setAttribute("errorCredentials", "errorCredentials");
                doGet(req, resp);
            }
        } catch (Exception exception) {
            LOGGER.error("Error validating credentials.", exception);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/error");
            dispatcher.forward(req, resp);
        }
    }
}
