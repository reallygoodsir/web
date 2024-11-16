<%@ page import="org.apache.logging.log4j.LogManager, org.apache.logging.log4j.Logger" %>
<%@ page session="false" %>
<%
    Logger logger = LogManager.getLogger("admin.jsp");
    try {
        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.info("No session. Return login page.");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <style>
        body {
            background: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            font-family: 'Arial', sans-serif;
            margin: 0;
        }
        .login-container {
            background: #fff;
            padding: 20px 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            text-align: center;
        }
        .login-container h2 {
            margin-bottom: 20px;
            color: #333;
        }
        .error-message {
            color: #d8000c;
            background-color: #ffd2d2;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .login-container input[type="text"],
        .login-container input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
        }
        .login-container button {
            width: 100%;
            padding: 10px;
            background: #4CAF50;
            color: #fff;
            border: none; <!-- fixed border property -->
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            transition: background 0.3s ease;
        }
        .login-container button:hover {
            background: #45a049;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <%
            String errorCredentials = (String) request.getAttribute("errorCredentials");
            if (errorCredentials != null && !errorCredentials.isEmpty()) {
        %>
          <div class="error-message" id="error-message">
              You entered an incorrect username or password. Please try again.
              Made with JSP
          </div>
        <%
            }
        %>
        <h2>Login</h2>
        <form action="admin" method="POST">
            <input type="text" id="username" name="username" placeholder="Enter your username" required>
            <input type="password" id="password" name="password" placeholder="Enter your password" required>
            <button type="submit">Login</button>
        </form>
    </div>
</body>
</html>
<%
        } else {
            logger.info("Session exists. Navigate to questions page.");
            response.sendRedirect("http://localhost:8080/servlets-quiz/questions");
        }
    } catch (Exception exception) {
        logger.error("Error showing admin login page.", exception);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error");
        dispatcher.forward(request, response);
    }
%>
