package crud.employees.app.dao;

import crud.employees.app.models.Employee;

import java.util.*;
import java.sql.*;

public class EmployeeDao {
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/people_db", "root", "root");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int save(Employee employee) {
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("insert into user905(name,password,email,country) values (?,?,?,?)");
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getPassword());
            ps.setString(3, employee.getEmail());
            ps.setString(4, employee.getCountry());
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (SQLException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public int update(Employee employee) {
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(
                    "update user905 set name=?,password=?,email=?,country=? where id=?");
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getPassword());
            ps.setString(3, employee.getEmail());
            ps.setString(4, employee.getCountry());
            ps.setInt(5, employee.getId());
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (SQLException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public void delete(int id) {
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("delete from user905 where id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (SQLException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public Employee getEmployeeById(int id) {
        Employee employee = new Employee();
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("select * from user905 where id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                employee.setId(rs.getInt(1));
                employee.setName(rs.getString(2));
                employee.setPassword(rs.getString(3));
                employee.setEmail(rs.getString(4));
                employee.setCountry(rs.getString(5));
            }
            return employee;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (SQLException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("select * from user905");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getInt(1));
                employee.setName(rs.getString(2));
                employee.setPassword(rs.getString(3));
                employee.setEmail(rs.getString(4));
                employee.setCountry(rs.getString(5));
                employees.add(employee);
            }
            return employees;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (SQLException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
