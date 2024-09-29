package file.management.servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DownloadImageServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("image/jpeg");
        ServletOutputStream out = response.getOutputStream();
        FileInputStream fin = new FileInputStream("d:\\data\\Java\\apache-tomcat-9.0.93\\files\\cats.jpg");

        BufferedInputStream bin = new BufferedInputStream(fin);
        BufferedOutputStream bout = new BufferedOutputStream(out);
        int ch;
        while ((ch = bin.read()) != -1) {
            bout.write(ch);
        }
        bin.close();
        fin.close();
        bout.close();
        out.close();
    }
}