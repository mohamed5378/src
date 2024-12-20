package payroll;

import employee.Employee;
import strategy.PayrollStrategy;

public class PayrollSystem {
    private static PayrollSystem instance;
    private PayrollStrategy payrollStrategy;

    private PayrollSystem() {}

    public static PayrollSystem getInstance() {
        if (instance == null) {
            instance = new PayrollSystem();
        }
        return instance;
    }

    public void setPayrollStrategy(PayrollStrategy payrollStrategy) {
        this.payrollStrategy = payrollStrategy;
    }

    public double calculatePay(Employee employee) {
        if (payrollStrategy == null) {
            throw new IllegalStateException("Payroll strategy not set.");
        }
        return payrollStrategy.calculatePay(employee);
    }
}
