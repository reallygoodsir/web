package org.cars.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(value = "/methods")
public class MethodsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Start doGet >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("Start reading request data in doGet >>>>>>>>>>>>>>>>>");
        System.out.println("Request methods:\n");
        System.out.println("Method: " + req.getMethod()); // +
        System.out.println("Header: " + req.getHeader("header")); // -
        System.out.println("DateHeader: " + req.getDateHeader("date-header")); // -
        System.out.println("RequestAttribute: " + req.getAttribute("attribute")); // -
        System.out.println("AuthType: " + req.getAuthType()); // -

        System.out.println("QueryString: " + req.getQueryString()); // +
        String value1 = req.getParameter("key1"); // +
        System.out.println("Parameter key1 value: " + value1); // +
        String value2 = req.getParameter("key2"); // +
        System.out.println("Parameter key2 value: " + value2); // +
        Map<String, String[]> parameterMap = req.getParameterMap(); // +
        for (String key : parameterMap.keySet()) {
            System.out.println("Parameter key " + key + " and value " + Arrays.toString(parameterMap.get(key))); // +
        }
        Enumeration<String> parameterNames = req.getParameterNames(); // +
        while (parameterNames.hasMoreElements()) {
            System.out.println("Parameter name " + parameterNames.nextElement()); // +
        }

        Cookie[] cookies = req.getCookies(); //+
        for (Cookie cookie : cookies) {
            System.out.println("Cookie " + cookie.getName() + " " + cookie.getValue()); //+
        }

        System.out.println("PathInfo: " + req.getPathInfo()); // -
        System.out.println("PathTranslated: " + req.getPathTranslated()); // -
        System.out.println("ContextPath: " + req.getContextPath()); // +
        System.out.println("RequestURI: " + req.getRequestURI()); // +
        System.out.println("RequestURL: " + req.getRequestURL()); // +
        System.out.println("ServletPath: " + req.getServletPath()); // +
        System.out.println("EncodedURL: " + req.getRequestURL()); // -

        String localAddr = req.getLocalAddr();
        System.out.println("Local Address " + localAddr);
        String localName = req.getLocalName();
        System.out.println("Local name " + localName);
        int localPort = req.getLocalPort();
        System.out.println("Local port " + localPort);
        String protocol = req.getProtocol();
        System.out.println("Protocol " + protocol);
        String remoteAddr = req.getRemoteAddr();
        System.out.println("Remote Address " + remoteAddr);
        String scheme = req.getScheme();
        System.out.println("Scheme " + scheme);
        String serverName = req.getServerName();
        System.out.println("Server name " + serverName);
        int serverPort = req.getServerPort();
        System.out.println("Server port " + serverPort);

        Locale locale = req.getLocale();
        System.out.println("Locale country " + locale.getCountry());
        System.out.println("Locale language " + locale.getDisplayLanguage());

        System.out.println("End reading request data in doGet >>>>>>>>>>>>>>>>>");

        System.out.println();

        System.out.println("Start adding/reading session data in doGet >>>>>>>>>>>>>>>>>");
        HttpSession session = req.getSession(true);
        session.putValue("session-attribute-key1", "session-attribute-value1");
        session.setAttribute("session-attribute-key2", "session-attribute-value2");
        session.setMaxInactiveInterval(20);

        String sessionAttributeKey1 = (String) session.getAttribute("session-attribute-key1");
        System.out.println("session-attribute-key1 " + sessionAttributeKey1);
        String sessionAttributeKey2 = (String) session.getAttribute("session-attribute-key2");
        System.out.println("session-attribute-key2 " + sessionAttributeKey2);
        System.out.println("Start adding/reading session data in doGet >>>>>>>>>>>>>>>>>");

        Cookie cookie = new Cookie("cookie-key", "cookie-value");
        resp.addCookie(cookie);
        resp.setHeader("header-test", "header-test-value");
        boolean containsHeaderTest = resp.containsHeader("header-test");
        System.out.println("ContainsHeader named \"header-test\"? " + containsHeaderTest);
        resp.setStatus(200);
        resp.setDateHeader("date-header", 100000);

        PrintWriter writer = resp.getWriter();
        writer.println("<h1>response methods completed</h1>\n" +
                "<form method=\"POST\">\n" +
                "<input type=\"submit\" value=\"request methods\"/>" +
                "</form>\n" +
                "</br></br>" +
                "</div>");

        System.out.println("End doGet >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println();
        System.out.println("Start doPost >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("SessionMethods:\n");
        HttpSession session = req.getSession();
        System.out.println("NewSession: " + session.isNew());
        System.out.println("SessionIdFromURL: " + req.isRequestedSessionIdFromURL()); // ?
        System.out.println("SessionCreationTime: " + session.getCreationTime());
        System.out.println("MaxInactiveTime: " + session.getMaxInactiveInterval());
        System.out.println("LastAccessedTime: " + session.getLastAccessedTime());
        System.out.println("ServletContext: " + session.getServletContext());
        System.out.println("SessionContext: " + session.getSessionContext());
        System.out.println("SessionValue: " + session.getValue("session-attribute-key1"));
        System.out.println("SessionAttribute: " + session.getAttribute("session-attribute-key2"));
        PrintWriter writer = resp.getWriter();
        writer.println("<h1>request/session methods completed</h1>\n" +
                "<form method=\"GET\">\n" +
                "<input type=\"submit\" value=\"response methods\"/>" +
                "</form>\n" +
                "</br></br>" +
                "</div>");
        System.out.println("End doPost >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
