package builder;

import employee.Employee;

public interface EmployeeBuilder {
    
    public EmployeeBuilder setID(int id);

    public EmployeeBuilder setName(String name);

    public EmployeeBuilder setDepartment(String department);

    public EmployeeBuilder setRole(String role);

    public EmployeeBuilder setWrkingHoursPerWeek(double workingHoursPerWeek);

    public EmployeeBuilder setSalary(double salary);

    public Employee getEmployee();
    
}
