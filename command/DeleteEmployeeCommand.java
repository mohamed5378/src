package command;

import manager.EmployeeManager;

public class DeleteEmployeeCommand implements Command {
    private final int employeeId;

    public DeleteEmployeeCommand(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public void execute() {
        EmployeeManager.getInstance().deleteEmployee(employeeId);
    }
}
