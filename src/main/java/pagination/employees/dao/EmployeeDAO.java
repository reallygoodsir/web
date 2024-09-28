package pagination.employees.dao;

import pagination.employees.models.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/people_db", "root", "root");
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return con;
    }

    public List<Employee> getRecords(int start, int total) {
        List<Employee> list = new ArrayList<>();
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("select * from pagination limit " + (start - 1) + "," + total);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getInt(1));
                employee.setName(resultSet.getString(2));
                employee.setSalary(resultSet.getFloat(3));
                list.add(employee);
            }
        } catch (Exception exception) {
            System.out.println(exception);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
        return list;
    }
}
