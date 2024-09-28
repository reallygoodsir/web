package crud.employees.app.servlets;

import crud.employees.app.dao.EmployeeDao;
import crud.employees.app.models.Employee;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EmployeeServlet extends HttpServlet {
    private String addEmployeeHtml;

    private String viewEmployeesHtml;

    private String updateEmployeesHtml;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            InputStream inputStreamAddEmployeeHtml = getClass().getClassLoader().getResourceAsStream("add-employee.html");
            if (inputStreamAddEmployeeHtml != null) {
                addEmployeeHtml = IOUtils.toString(inputStreamAddEmployeeHtml, StandardCharsets.UTF_8);
                System.out.println("AddEmployeesHtml: " + addEmployeeHtml);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            InputStream inputStreamViewEmployeesHtml = getClass().getClassLoader().getResourceAsStream("view-employees.html");
            if (inputStreamViewEmployeesHtml != null) {
                viewEmployeesHtml = IOUtils.toString(inputStreamViewEmployeesHtml, StandardCharsets.UTF_8);
                System.out.println("ViewEmployeesHtml: " + viewEmployeesHtml);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            InputStream inputStreamUpdateEmployeesHtml = getClass().getClassLoader().getResourceAsStream("update-employee.html");
            if (inputStreamUpdateEmployeesHtml != null) {
                updateEmployeesHtml = IOUtils.toString(inputStreamUpdateEmployeesHtml, StandardCharsets.UTF_8);
                System.out.println("UpdateEmployeesHtml: " + updateEmployeesHtml);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Start doGet");
        String servletPath = request.getServletPath();
        if ("/add".equalsIgnoreCase(servletPath)) {
            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            writer.println(addEmployeeHtml);
        } else if ("/view".equalsIgnoreCase(servletPath)) {
            EmployeeDao employeeDao = new EmployeeDao();
            List<Employee> list = employeeDao.getAllEmployees();
            System.out.println("Employees size: " + list.size());

            StringBuilder employeesList = new StringBuilder();
            for (Employee e : list) {
                employeesList.append("<tr>");
                employeesList.append("<td>").append(e.getId()).append("</td>");
                employeesList.append("<td>").append(e.getName()).append("</td>");
                employeesList.append("<td>").append(e.getPassword()).append("</td>");
                employeesList.append("<td>").append(e.getEmail()).append("</td>");
                employeesList.append("<td>").append(e.getCountry()).append("</td>");
                employeesList.append("<td><a href='update?id=").append(e.getId()).append("'>edit</a></td>");
                employeesList.append("<td><a href='delete?id=").append(e.getId()).append("'>delete</a></td>");
                employeesList.append("</tr>");
            }
            System.out.println("EmployeesList: " + employeesList);

            String viewEmployeesHtmlResponse = viewEmployeesHtml.replace("EmployeesListHTML", employeesList.toString());
            System.out.println("ViewEmployeesHtml after replacement: " + viewEmployeesHtmlResponse);

            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            writer.println(viewEmployeesHtmlResponse);
            writer.close();
        } else if ("/update".equalsIgnoreCase(servletPath)) {
            String sid = request.getParameter("id");
            int id = Integer.parseInt(sid);
            EmployeeDao employeeDao = new EmployeeDao();
            Employee employee = employeeDao.getEmployeeById(id);

            String updateEmployeesHtmlResponse = updateEmployeesHtml.replace("id_value_replace", String.valueOf(employee.getId()))
                    .replace("name_value_replace", employee.getName())
                    .replace("password_value_replace", employee.getPassword())
                    .replace("email_value_replace", employee.getEmail());
            System.out.println("UpdateEmployeesHtml after replacement: " + updateEmployeesHtmlResponse);

            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            writer.println(updateEmployeesHtmlResponse);
            writer.close();
        } else if ("/delete".equalsIgnoreCase(servletPath)) {
            doDelete(request, response);
        }
        System.out.println("End doGet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Start doPost");
        String servletPath = request.getServletPath();
        if ("/add".equalsIgnoreCase(servletPath)) {
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String country = request.getParameter("country");

            Employee e = new Employee();
            e.setName(name);
            e.setPassword(password);
            e.setEmail(email);
            e.setCountry(country);

            EmployeeDao employeeDao = new EmployeeDao();
            int status = employeeDao.save(e);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            if (status > 0) {
                out.println("<p>Record saved successfully!</p>");
                out.println(addEmployeeHtml);
            } else {
                out.println("Sorry! unable to save record");
            }
            out.close();
        } else if ("/update".equalsIgnoreCase(servletPath)) {
            doPut(request, response);
        }
        System.out.println("End doPost");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Start doPut");
        String sid = request.getParameter("id");
        int id = Integer.parseInt(sid);
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String country = request.getParameter("country");

        Employee employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        employee.setPassword(password);
        employee.setEmail(email);
        employee.setCountry(country);

        EmployeeDao employeeDao = new EmployeeDao();
        int status = employeeDao.update(employee);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if (status > 0) {
            response.setStatus(response.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", "http://localhost:8080/employees-web-app/view");
        } else {
            out.println("Sorry! unable to update record");
        }
        out.close();
        System.out.println("End doPut");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Start doDelete");
        String sid = request.getParameter("id");
        int id = Integer.parseInt(sid);
        EmployeeDao employeeDao = new EmployeeDao();
        employeeDao.delete(id);
        response.sendRedirect("view");
        System.out.println("End doDelete");
    }
}
