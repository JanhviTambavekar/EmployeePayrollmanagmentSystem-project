package com.payrollapplication.payroll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService 
{
	 @Autowired
	    private EmployeeRepository repository;
	    public EmployeeService(EmployeeRepository repository) {
	        this.repository = repository;
	    }

	    public List<Employee> getAllEmployees() {
	        return repository.findAll();
	    }

	    public Employee getEmployeeById(Long id) {
	        return repository.findById(id)
	                .orElseThrow(() -> new EmployeeNotFoundException(id));
	    }
	    public void deleteEmployee(Long id) {
	        repository.deleteById(id);
	    }

	    public Employee saveEmployee(Employee employee) {
	        return repository.save(employee);
	    }

	    public Employee updateEmployee(Long id, Employee newEmployee) {
	        return repository.findById(id)
	                .map(employee -> {
	                    employee.setFirstName(newEmployee.getFirstName());
	                    employee.setLastName(newEmployee.getLastName());
	                    employee.setDepartment(newEmployee.getDepartment());
	                    employee.setSalary(newEmployee.getSalary());
	                    employee.setEmail(newEmployee.getEmail());
	                    employee.setHireDate(newEmployee.getHireDate());
	                    return repository.save(employee);
	                })
	                .orElseGet(() -> {
	                    newEmployee.setId(id);
	                    return repository.save(newEmployee);
	                });
	    }
	    
}
