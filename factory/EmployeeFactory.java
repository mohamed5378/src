package factory;

import builder.EmployeeDirector;
import builder.FullTimeEmployeeBuilder;
import builder.PartTimeEmployeeBuilder;
import employee.Employee;

public 
/**
 * This class represents the EmployeeFactory entity in the system.
 * 
 * @author Igwilo Chidumebi
 */
class EmployeeFactory {
    public static Employee createEmployee(String type, int id, String name, 
            String department, String role, double workingHoursPerWeek, double salary) {
        
        if (type.equals("PartTime")) {
            return EmployeeDirector.buildEmployee(new PartTimeEmployeeBuilder(), id, name, department, role, workingHoursPerWeek, salary);
        } else if (type.equals("FullTime")) {
            return EmployeeDirector.buildEmployee(new FullTimeEmployeeBuilder(), id, name, department, role, workingHoursPerWeek, salary);
        } else {
            throw new IllegalArgumentException("Invalid Argument");
        } 
    }
}
