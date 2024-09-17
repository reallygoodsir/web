package org.cars.servlets;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

public class FileServlet extends HttpServlet {
    private boolean isMultipart;
    private String filePath = "d:\\data\\Java\\apache-tomcat-9.0.93\\files\\";
    private int maxFileSize = 50 * 1024;
    private int maxMemSize = 4 * 1024;
    private File file ;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("<html>\n" +
                "   <head>\n" +
                "      <title>File Uploading Form</title>\n" +
                "   </head>\n" +
                "   \n" +
                "   <body>\n" +
                "      <h3>File Upload:</h3>\n" +
                "      Select a file to upload: <br />\n" +
                "      <form action = \"file-upload\" method = \"post\" enctype = \"multipart/form-data\">\n" +
                "         <input type = \"file\" name = \"file\" size = \"50\" />\n" +
                "         <br />\n" +
                "         <input type = \"submit\" value = \"Upload File\" />\n" +
                "      </form>\n" +
                "   </body>\n" +
                "</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        isMultipart = ServletFileUpload.isMultipartContent(request);
        response.setContentType("text/html");
        java.io.PrintWriter out = response.getWriter( );

        if( !isMultipart ) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet upload</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p>No file uploaded</p>");
            out.println("</body>");
            out.println("</html>");
            return;
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();

        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);

        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File(filePath));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // maximum file size to be uploaded.
        upload.setSizeMax( maxFileSize );

        try {
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);

            // Process the uploaded file items
            Iterator i = fileItems.iterator();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet upload</title>");
            out.println("</head>");
            out.println("<body>");

            while ( i.hasNext () ) {
                FileItem fi = (FileItem)i.next();
                if ( !fi.isFormField () ) {
                    // Get the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    String contentType = fi.getContentType();
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();

                    // Write the file
                    if( fileName.lastIndexOf("\\") >= 0 ) {
                        file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\"))) ;
                    } else {
                        file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
                    }
                    System.out.println("AbsolutePath " + file.getAbsolutePath());
                    fi.write( file ) ;
                    out.println("Uploaded Filename: " + fileName + "<br>");
                }
            }
            out.println("</body>");
            out.println("</html>");
        } catch(Exception ex) {
            System.out.println(ex);
        }
    }
}
