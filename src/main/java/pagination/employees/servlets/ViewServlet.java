package pagination.employees.servlets;

import org.apache.commons.io.IOUtils;
import pagination.employees.dao.EmployeeDAO;
import pagination.employees.models.Employee;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ViewServlet extends HttpServlet {
    private String viewEmployees;

    @Override
    public void init() {
        try {
            InputStream viewEmployeesHTML = getClass().getClassLoader().getResourceAsStream("view.html");
            if (viewEmployeesHTML != null) {
                viewEmployees = IOUtils.toString(viewEmployeesHTML, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int pageId;
        try {
            pageId = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            pageId = 1;
        }
        if (pageId <= 0) {
            pageId = 1;
        }

        int employeePageID = pageId;
        int maxEmployeePerPage = 4;
        if (employeePageID != 1) {
            employeePageID = employeePageID - 1;
            employeePageID = employeePageID * maxEmployeePerPage + 1;
        }

        EmployeeDAO employeeDAO = new EmployeeDAO();
        List<Employee> employeeList = employeeDAO.getRecords(employeePageID, maxEmployeePerPage);

        StringBuilder employeeListHTML = new StringBuilder();
        for (Employee employee : employeeList) {
            employeeListHTML.append("<tr>");
            employeeListHTML.append("<td>").append(employee.getId()).append("</td>");
            employeeListHTML.append("<td>").append(employee.getName()).append("</td>");
            employeeListHTML.append("<td>").append(employee.getSalary()).append("</td>");
            employeeListHTML.append("</tr>");
        }
        String viewEmployeesHTMLResponse = viewEmployees.replace("EmployeeList", employeeListHTML.toString())
                .replace("PageID", String.valueOf(pageId));

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println(viewEmployeesHTMLResponse);
        writer.close();
    }
}
