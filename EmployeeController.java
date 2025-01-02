package com.payrollapplication.payroll;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/employees")

public class EmployeeController {
	  private final EmployeeService service;
	    private final EmployeeModelAssembler assembler;

	    public EmployeeController(EmployeeService service, EmployeeModelAssembler assembler) {
	        this.service = service;
	        this.assembler = assembler;
	    }

	    @GetMapping
	    public CollectionModel<EntityModel<Employee>> all() {
	        List<EntityModel<Employee>> employees = service.getAllEmployees().stream()
	                .map(assembler::toModel)
	                .collect(Collectors.toList());

	        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
	    }

	    @PostMapping
	    public ResponseEntity<?> newEmployee(@Valid @RequestBody Employee newEmployee) {
	        // Save the new employee and return the created resource
	        EntityModel<Employee> entityModel = assembler.toModel(service.saveEmployee(newEmployee));
	        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	    }

	    @GetMapping("/{id}")
	    public EntityModel<Employee> one(@PathVariable Long id) {
	        Employee employee = service.getEmployeeById(id);
	        return assembler.toModel(employee);
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<?> replaceEmployee(@Valid @RequestBody Employee newEmployee, @PathVariable Long id) {
	        // Update the employee and return the updated resource
	        Employee updatedEmployee = service.updateEmployee(id, newEmployee);
	        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);
	        return ResponseEntity.ok(entityModel); // Use 200 OK status for updates
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
	        // Delete the employee
	        service.deleteEmployee(id);
	        return ResponseEntity.noContent().build(); // Return 204 No Content status
	    }
	}