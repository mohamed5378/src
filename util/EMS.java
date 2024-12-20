package util;

import command.AddEmployeeCommand;
import command.DeleteEmployeeCommand;
import command.UpdateEmployeeCommand;
import employee.Employee;
import employee.FullTimeEmployee;
import employee.PartTimeEmployee;
import manager.EmployeeManager;
import payroll.PayrollSystem;
import strategy.HourlyPayStrategy;
import strategy.MonthlyPayStrategy;

import java.util.List;

public class EMS {
    public static void main(String[] args) {
        // Get the EmployeeManager instance
        EmployeeManager manager = EmployeeManager.getInstance();

        // Adding a Full-Time Employee using Command Pattern
        System.out.println("\nAdding a Full-Time Employee...");
        Employee fullTimeEmployee = EmployeeManager.createEmployee(1, "Alice", "Engineering", "Developer", 40, 8000, "FullTime");
        AddEmployeeCommand addFullTimeCommand = new AddEmployeeCommand(fullTimeEmployee);
        addFullTimeCommand.execute();

        // Adding a Part-Time Employee using Command Pattern
        System.out.println("\nAdding a Part-Time Employee...");
        Employee partTimeEmployee = EmployeeManager.createEmployee(2, "Bob", "Marketing", "Analyst", 20, 4000, "PartTime");
        AddEmployeeCommand addPartTimeCommand = new AddEmployeeCommand(partTimeEmployee);
        addPartTimeCommand.execute();

        // Update an Employee using Command Pattern
        System.out.println("\nUpdating Employee...");
        Employee updatedEmployee = EmployeeManager.createEmployee(1, "Alice Johnson", "Engineering", "Senior Developer", 40, 9000, "FullTime");
        UpdateEmployeeCommand updateCommand = new UpdateEmployeeCommand(updatedEmployee);
        updateCommand.execute();

        // Fetch and display all employees
        System.out.println("\nFetching Employees...");
        List<Employee> employees = manager.getEmployees();
        for (Employee employee : employees) {
            System.out.println("ID: " + employee.getID() + ", Name: " + employee.getName() +
                    ", Department: " + employee.getDepartment() +
                    ", Role: " + employee.getRole() +
                    ", Hours: " + employee.getWorkingHoursPerWeek() +
                    ", Salary: " + employee.getSalary());
        }

        // Calculate Payroll using Strategy Pattern
        System.out.println("\nCalculating Payroll...");
        PayrollSystem payrollSystem = PayrollSystem.getInstance();
        for (Employee employee : employees) {
            if (employee instanceof FullTimeEmployee) {
                payrollSystem.setPayrollStrategy(new MonthlyPayStrategy());
            } else if (employee instanceof PartTimeEmployee) {
                payrollSystem.setPayrollStrategy(new HourlyPayStrategy());
            }
            double pay = payrollSystem.calculatePay(employee);
            System.out.println(employee.getName() + " - Calculated Pay: $" + pay);
        }

        // Delete an Employee using Command Pattern
        System.out.println("\nDeleting Employee...");
        DeleteEmployeeCommand deleteCommand = new DeleteEmployeeCommand(2); // Delete employee with ID 2
        deleteCommand.execute();

        // Fetch and display remaining employees
        System.out.println("\nFetching Employees After Deletion...");
        employees = manager.getEmployees();
        for (Employee employee : employees) {
            System.out.println("ID: " + employee.getID() + ", Name: " + employee.getName() +
                    ", Department: " + employee.getDepartment() +
                    ", Role: " + employee.getRole() +
                    ", Hours: " + employee.getWorkingHoursPerWeek() +
                    ", Salary: " + employee.getSalary());
        }
    }
}
