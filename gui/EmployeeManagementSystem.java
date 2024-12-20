package gui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class EmployeeManagementSystem {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/EMS";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root"; 

    public static String getDbUrl() {
        return DB_URL;
    }

    public static String getDbUser() {
        return DB_USER;
    }

    public static String getDbPassword() {
        return DB_PASSWORD;
    }

    public static void main(String[] args) {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            SwingUtilities.invokeLater(() -> new LoginPage());
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL Driver not found. Please ensure it's added to the project.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

class LoginPage {
    private JFrame frame;

    public LoginPage() {
        frame = new JFrame("Login");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(loginButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if ("admin".equals(username) && "admin".equals(password)) {
                frame.dispose();
                new MainPage();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class MainPage {
    private JFrame frame;
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public MainPage() {
        frame = new JFrame("Employee Management");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Table setup
        String[] columns = {"ID", "Name", "Department", "Role", "Working Hours", "Salary", "Type"};
        tableModel = new DefaultTableModel(columns, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Employee");
        JButton updateButton = new JButton("Update Employee");
        JButton removeButton = new JButton("Remove Employee");
        JButton refreshButton = new JButton("Refresh Table");
        JButton payrollButton = new JButton("View Payroll");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(payrollButton);

        // Add components to frame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Button listeners
        addButton.addActionListener(e -> showAddEmployeeDialog());
        updateButton.addActionListener(e -> showUpdateEmployeeDialog());
        removeButton.addActionListener(e -> showRemoveEmployeeDialog());
        refreshButton.addActionListener(e -> refreshTable());
        payrollButton.addActionListener(e -> showPayrollDetails());

        // Populate table initially
        refreshTable();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // Clear existing rows
        try (Connection conn = DriverManager.getConnection(EmployeeManagementSystem.getDbUrl(), EmployeeManagementSystem.getDbUser(), EmployeeManagementSystem.getDbPassword());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("department"));
                row.add(rs.getString("role"));
                row.add(rs.getDouble("working_hours_per_week"));
                row.add(rs.getDouble("salary"));
                row.add(rs.getString("type")); // Include the type column
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddEmployeeDialog() {
        JDialog dialog = new JDialog(frame, "Add Employee", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(7, 2));

        JTextField nameField = new JTextField();
        JTextField departmentField = new JTextField();
        JTextField roleField = new JTextField();
        JTextField workingHoursField = new JTextField();
        JTextField salaryField = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"FullTime", "PartTime"});
        JButton saveButton = new JButton("Save");

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Department:"));
        dialog.add(departmentField);
        dialog.add(new JLabel("Role:"));
        dialog.add(roleField);
        dialog.add(new JLabel("Working Hours:"));
        dialog.add(workingHoursField);
        dialog.add(new JLabel("Salary:"));
        dialog.add(salaryField);
        dialog.add(new JLabel("Type:"));
        dialog.add(typeBox);
        dialog.add(new JLabel());
        dialog.add(saveButton);

        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String department = departmentField.getText();
            String role = roleField.getText();
            double workingHours = Double.parseDouble(workingHoursField.getText());
            double salary = Double.parseDouble(salaryField.getText());
            String type = typeBox.getSelectedItem().toString();

            try (Connection conn = DriverManager.getConnection(EmployeeManagementSystem.getDbUrl(), EmployeeManagementSystem.getDbUser(), EmployeeManagementSystem.getDbPassword());
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO employees (name, department, role, working_hours_per_week, salary, type) VALUES (?, ?, ?, ?, ?, ?)")) {

                stmt.setString(1, name);
                stmt.setString(2, department);
                stmt.setString(3, role);
                stmt.setDouble(4, workingHours);
                stmt.setDouble(5, salary);
                stmt.setString(6, type);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(dialog, "Employee added successfully.");
                dialog.dispose();
                refreshTable();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error adding employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void showUpdateEmployeeDialog() {
        JDialog dialog = new JDialog(frame, "Update Employee", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(8, 2));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField departmentField = new JTextField();
        JTextField roleField = new JTextField();
        JTextField workingHoursField = new JTextField();
        JTextField salaryField = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"FullTime", "PartTime"});
        JButton updateButton = new JButton("Update");

        dialog.add(new JLabel("ID:"));
        dialog.add(idField);
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Department:"));
        dialog.add(departmentField);
        dialog.add(new JLabel("Role:"));
        dialog.add(roleField);
        dialog.add(new JLabel("Working Hours:"));
        dialog.add(workingHoursField);
        dialog.add(new JLabel("Salary:"));
        dialog.add(salaryField);
        dialog.add(new JLabel("Type:"));
        dialog.add(typeBox);
        dialog.add(new JLabel());
        dialog.add(updateButton);

        updateButton.addActionListener(e -> {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String department = departmentField.getText();
            String role = roleField.getText();
            double workingHours = Double.parseDouble(workingHoursField.getText());
            double salary = Double.parseDouble(salaryField.getText());
            String type = typeBox.getSelectedItem().toString();

            try (Connection conn = DriverManager.getConnection(EmployeeManagementSystem.getDbUrl(), EmployeeManagementSystem.getDbUser(), EmployeeManagementSystem.getDbPassword());
                 PreparedStatement stmt = conn.prepareStatement("UPDATE employees SET name = ?, department = ?, role = ?, working_hours_per_week = ?, salary = ?, type = ? WHERE id = ?")) {

                stmt.setString(1, name);
                stmt.setString(2, department);
                stmt.setString(3, role);
                stmt.setDouble(4, workingHours);
                stmt.setDouble(5, salary);
                stmt.setString(6, type);
                stmt.setInt(7, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(dialog, "Employee updated successfully.");
                dialog.dispose();
                refreshTable();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error updating employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void showRemoveEmployeeDialog() {
        JDialog dialog = new JDialog(frame, "Remove Employee", true);
        dialog.setSize(300, 150);
        dialog.setLayout(new GridLayout(2, 2));

        JTextField idField = new JTextField();
        JButton removeButton = new JButton("Remove");

        dialog.add(new JLabel("ID:"));
        dialog.add(idField);
        dialog.add(new JLabel());
        dialog.add(removeButton);

        removeButton.addActionListener(e -> {
            int id = Integer.parseInt(idField.getText());

            try (Connection conn = DriverManager.getConnection(EmployeeManagementSystem.getDbUrl(), EmployeeManagementSystem.getDbUser(), EmployeeManagementSystem.getDbPassword());
                 PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM employees WHERE id = ?")) {

                deleteStmt.setInt(1, id);
                deleteStmt.executeUpdate();

                // Reset AUTO_INCREMENT
                try (Statement alterStmt = conn.createStatement()) {
                    alterStmt.executeUpdate("ALTER TABLE employees AUTO_INCREMENT = 1");
                }

                JOptionPane.showMessageDialog(dialog, "Employee removed successfully.");
                dialog.dispose();
                refreshTable();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error removing employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void showPayrollDetails() {
        StringBuilder payrollDetails = new StringBuilder("Payroll Details:\n\n");

        try (Connection conn = DriverManager.getConnection(EmployeeManagementSystem.getDbUrl(), EmployeeManagementSystem.getDbUser(), EmployeeManagementSystem.getDbPassword());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {

            while (rs.next()) {
                String name = rs.getString("name");
                double workingHours = rs.getDouble("working_hours_per_week");
                double salary = rs.getDouble("salary");
                double hourlyRate = salary / (workingHours > 0 ? workingHours : 1);
                double weeklyPay = hourlyRate * workingHours;

                payrollDetails.append(name).append(" - Weekly Pay: $").append(String.format("%.2f", weeklyPay)).append("\n");
            }

            JOptionPane.showMessageDialog(frame, payrollDetails.toString(), "Payroll Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching payroll details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
