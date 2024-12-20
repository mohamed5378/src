package strategy;

import employee.Employee;

public class HourlyPayStrategy implements PayrollStrategy {
    @Override
    public double calculatePay(Employee employee) {
        return employee.getWorkingHoursPerWeek() * (employee.getSalary() / employee.getWorkingHoursPerWeek());
    }
}
