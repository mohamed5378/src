package manager;

import Connection.DatabaseConnectionManager;
import employee.Employee;
import employee.FullTimeEmployee;
import employee.PartTimeEmployee;
import payroll.PayrollSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManager {
    private static final EmployeeManager instance = new EmployeeManager();
    private final Connection connection;
    private final PayrollSystem payrollSystem;

    private EmployeeManager() {
        connection = DatabaseConnectionManager.getInstance().getConnection();
        if (connection == null) {
            System.err.println("Database connection is null. Ensure the database is configured correctly.");
        }
        payrollSystem = PayrollSystem.getInstance(); // PayrollSystem singleton
    }

    public static EmployeeManager getInstance() {
        return instance;
    }

    public boolean addEmployee(Employee employee) {
        String query = "INSERT INTO employees (name, department, role, working_hours_per_week, salary, type) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getDepartment());
            stmt.setString(3, employee.getRole());
            stmt.setDouble(4, employee.getWorkingHoursPerWeek());
            stmt.setDouble(5, employee.getSalary());
            stmt.setString(6, (employee instanceof FullTimeEmployee) ? "FullTime" : "PartTime");
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }

    public boolean updateEmployee(Employee employee) {
        String query = "UPDATE employees SET name = ?, department = ?, role = ?, working_hours_per_week = ?, salary = ?, type = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getDepartment());
            stmt.setString(3, employee.getRole());
            stmt.setDouble(4, employee.getWorkingHoursPerWeek());
            stmt.setDouble(5, employee.getSalary());
            stmt.setString(6, (employee instanceof FullTimeEmployee) ? "FullTime" : "PartTime");
            stmt.setInt(7, employee.getID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEmployee(int employeeId) {
        String query = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }

    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String department = rs.getString("department");
                String role = rs.getString("role");
                double workingHours = rs.getDouble("working_hours_per_week");
                double salary = rs.getDouble("salary");
                String type = rs.getString("type"); // Retrieve the employee type correctly

                Employee employee = createEmployee(id, name, department, role, workingHours, salary, type);
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees: " + e.getMessage());
        }
        return employees;
    }

    public static Employee createEmployee(int id, String name, String department, String role, 
                                          double workingHours, double salary, String type) {
        if (type.equalsIgnoreCase("FullTime")) {
            return new FullTimeEmployee(id, name, department, role, workingHours, salary);
        } else if (type.equalsIgnoreCase("PartTime")) {
            return new PartTimeEmployee(id, name, department, role, workingHours, salary);
        } else {
            throw new IllegalArgumentException("Invalid employee type: " + type);
        }
    }
}
