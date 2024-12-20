package command;

import employee.Employee;
import manager.EmployeeManager;

public class AddEmployeeCommand implements Command {
    private final Employee employee;

    public AddEmployeeCommand(Employee employee) {
        this.employee = employee;
    }

    @Override
    public void execute() {
        EmployeeManager.getInstance().addEmployee(employee);
    }
}
