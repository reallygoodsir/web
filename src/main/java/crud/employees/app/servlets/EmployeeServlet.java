package crud.employees.app.servlets;

import crud.employees.app.dao.EmpDao;
import crud.employees.app.models.Emp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class EmployeeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/add".equalsIgnoreCase(servletPath)) {
            PrintWriter writer = response.getWriter();
            writer.println("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "\n" +
                    "<head>\n" +
                    "    <meta charset=\"ISO-8859-1\">\n" +
                    "    <title>Insert title here</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1>Add New Employee</h1>\n" +
                    "<form action=\"http://localhost:8080/employees-web-app/add\" method=\"post\">\n" +
                    "    <table>\n" +
                    "        <tr><td>Name:</td><td><input type=\"text\" name=\"name\"/></td></tr>\n" +
                    "        <tr><td>Password:</td><td><input type=\"password\" name=\"password\"/></td></tr>\n" +
                    "        <tr><td>Email:</td><td><input type=\"email\" name=\"email\"/></td></tr>\n" +
                    "        <tr><td>Country:</td><td>\n" +
                    "            <select name=\"country\" style=\"width:150px\">\n" +
                    "\n" +
                    "                <option>India</option>\n" +
                    "                <option>USA</option>\n" +
                    "                <option>UK</option>\n" +
                    "                <option>Other</option>\n" +
                    "            </select>\n" +
                    "        </td></tr>\n" +
                    "        <tr><td colspan=\"2\"><input type=\"submit\" value=\"Save Employee\"/></td></tr>\n" +
                    "    </table>\n" +
                    "</form>\n" +
                    "\n" +
                    "<br/>\n" +
                    "<a href=\"view\">view employees</a>\n" +
                    "\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>");

        } else  if ("/view".equalsIgnoreCase(servletPath)) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<form method=\"get\" action=\"http://localhost:8080/employees-web-app/add\">\n" +
                    "    <input type=\"submit\" value=\"Add New Employee\">\n" +
                    "</form>");
            out.println("<h1>Employees List</h1>");

            List<Emp> list = EmpDao.getAllEmployees();

            out.print("<table border='1' width='100%'");
            out.print("<tr><th>Id</th><th>Name</th><th>Password</th><th>Email</th><th>Country</th> <th>Edit</th><th>Delete</th></tr>");
            for (Emp e : list) {
                out.print("<tr><td>" + e.getId() + "</td><td>" + e.getName() + "</td><td>" + e.getPassword() + "</td> <td>" + e.getEmail() + "</td><td>" + e.getCountry() + "</td><td><a href='update?id=" + e.getId() + "'>edit</a></td> <td><a href='delete?id=" + e.getId() + "'>delete</a></td></tr>");
            }
            out.print("</table>");

            out.close();
        } else if("/update".equalsIgnoreCase(servletPath)){
            response.setContentType("text/html");
            PrintWriter out=response.getWriter();
            out.println("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "\n" +
                    "<head>\n" +
                    "    <meta charset=\"ISO-8859-1\">\n" +
                    "    <title>Insert title here</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\n");
            out.println("<h1>Update Employee</h1>");
            String sid=request.getParameter("id");
            int id=Integer.parseInt(sid);

            Emp e=EmpDao.getEmployeeById(id);

            out.print("<form action=\"http://localhost:8080/employees-web-app/update\" method=\"post\">");
            out.print("<table>");
            out.print("<tr><td></td><td><input type='hidden' name='id' value='"+e.getId()+"'/></td></tr>");
            out.print("<tr><td>Name:</td><td><input type='text' name='name' value='"+e.getName()+"'/></td></tr>");
            out.print("<tr><td>Password:</td><td><input type='password' name='password' value='"+e.getPassword()+"'/> </td></tr>");
            out.print("<tr><td>Email:</td><td><input type='email' name='email' value='"+e.getEmail()+"'/></td></tr>");
            out.print("<tr><td>Country:</td><td>");
            out.print("<select name='country' style='width:150px'>");
            out.print("<option>India</option>");
            out.print("<option>USA</option>");
            out.print("<option>UK</option>");
            out.print("<option>Other</option>");
            out.print("</select>");
            out.print("</td></tr>");
            out.print("<tr><td colspan='2'>" +
                    "<input type='submit' value='Edit & Save '/>" +
                    "</td></tr>");
            out.print("</table>");
            out.print("</form>\n");
            out.print("\n" +
                    "<br/>\n" +
                    "<a href=\"view\">view employees</a>\n" +
                    "\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>");


            out.close();
        } else if ("/delete".equalsIgnoreCase(servletPath)){
            doDelete(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/add".equalsIgnoreCase(servletPath)) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String country = request.getParameter("country");

            Emp e = new Emp();
            e.setName(name);
            e.setPassword(password);
            e.setEmail(email);
            e.setCountry(country);

            int status = EmpDao.save(e);
            if (status > 0) {
                out.println("<p>Record saved successfully!</p>");
                out.println("<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "\n" +
                        "<head>\n" +
                        "    <meta charset=\"ISO-8859-1\">\n" +
                        "    <title>Insert title here</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "<h1>Add New Employee</h1>\n" +
                        "<form action=\"http://localhost:8080/employees-web-app/add\" method=\"post\">\n" +
                        "    <table>\n" +
                        "        <tr><td>Name:</td><td><input type=\"text\" name=\"name\"/></td></tr>\n" +
                        "        <tr><td>Password:</td><td><input type=\"password\" name=\"password\"/></td></tr>\n" +
                        "        <tr><td>Email:</td><td><input type=\"email\" name=\"email\"/></td></tr>\n" +
                        "        <tr><td>Country:</td><td>\n" +
                        "            <select name=\"country\" style=\"width:150px\">\n" +
                        "\n" +
                        "                <option>India</option>\n" +
                        "                <option>USA</option>\n" +
                        "                <option>UK</option>\n" +
                        "                <option>Other</option>\n" +
                        "            </select>\n" +
                        "        </td></tr>\n" +
                        "        <tr><td colspan=\"2\"><input type=\"submit\" value=\"Save Employee\"/></td></tr>\n" +
                        "    </table>\n" +
                        "</form>\n" +
                        "\n" +
                        "<br/>\n" +
                        "<a href=\"view\">view employees</a>\n" +
                        "\n" +
                        "</body>\n" +
                        "\n" +
                        "</html>");
            } else {
                out.println("Sorry! unable to save record");
            }
            out.close();
        } else if ("/update".equalsIgnoreCase(servletPath)){
            doPut(request, response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        if ("/view".equalsIgnoreCase(servletPath)) {
//
//        } else  if ("/add".equalsIgnoreCase(servletPath)) {
//
//        } else  if ("/update".equalsIgnoreCase(servletPath)) {
//
//        } else  if ("/delete".equalsIgnoreCase(servletPath)) {
//
//        }
        System.out.println("doput");
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();

        String sid=request.getParameter("id");
        int id=Integer.parseInt(sid);
        String name=request.getParameter("name");
        String password=request.getParameter("password");
        String email=request.getParameter("email");
        String country=request.getParameter("country");

        Emp e=new Emp();
        e.setId(id);
        e.setName(name);
        e.setPassword(password);
        e.setEmail(email);
        e.setCountry(country);

        int status=EmpDao.update(e);
        if(status>0){
            response.setStatus(response.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", "http://localhost:8080/employees-web-app/view");
        }else{
            out.println("Sorry! unable to update record");
        }

        out.close();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("dodelete");
        String sid=request.getParameter("id");
        int id=Integer.parseInt(sid);
        EmpDao.delete(id);
        response.sendRedirect("view");
    }
}
