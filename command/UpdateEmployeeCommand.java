package command;

import employee.Employee;
import manager.EmployeeManager;

public class UpdateEmployeeCommand implements Command {
    private final Employee employee;

    public UpdateEmployeeCommand(Employee employee) {
        this.employee = employee;
    }

    @Override
    public void execute() {
        EmployeeManager.getInstance().updateEmployee(employee);
    }
}

