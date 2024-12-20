package strategy;

import employee.Employee;

public class MonthlyPayStrategy implements PayrollStrategy {
    @Override
    public double calculatePay(Employee employee) {
        return employee.getSalary();
    }
}
