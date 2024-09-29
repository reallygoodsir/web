package file.management.servlets;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DownloadPDFServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "application/pdf");
        response.setHeader("Content-disposition", "inline; filename='my-generated.pdf'");

        try {
            ServletOutputStream outputStream = response.getOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph("Download PDF file"));
            document.add(new Paragraph("Write data to pdf"));
            document.close();
        } catch (DocumentException de) {
            throw new IOException(de.getMessage());
        }
    }
}
