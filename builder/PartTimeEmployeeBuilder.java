package builder;

import employee.Employee;
import employee.PartTimeEmployee;

public class PartTimeEmployeeBuilder implements EmployeeBuilder {
    private Employee employee;

    public PartTimeEmployeeBuilder() {this.employee = new PartTimeEmployee();}

    @Override
    public EmployeeBuilder setID(int id) {
        employee.setID(id);
        return this;
    }

    @Override
    public EmployeeBuilder setName(String name) {
        employee.setName(name);
        return this;
    }

    @Override
    public EmployeeBuilder setDepartment(String department) {
        employee.setDepartment(department);
        return this;
    }

    @Override
    public EmployeeBuilder setRole(String role) {
        employee.setRole(role);
        return this;
    }

    @Override
    public EmployeeBuilder setWrkingHoursPerWeek(double workingHoursPerWeek) {
        employee.setWrkingHoursPerWeek(workingHoursPerWeek);
        return this;
    }

    @Override
    public EmployeeBuilder setSalary(double salary) {
        employee.setSalary(salary);
        return this;
    }

    @Override
    public Employee getEmployee() {
        return employee;
    }
}