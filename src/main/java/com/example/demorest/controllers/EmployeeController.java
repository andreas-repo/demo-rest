package com.example.demorest.controllers;

import com.example.demorest.assembler.EmployeeModelAssembler;
import com.example.demorest.dto.EmployeeDTO;
import com.example.demorest.mapping.EmployeeMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {
    private final EmployeeMappingService employeeMappingService;
    private final EmployeeModelAssembler assembler;

    @Autowired
    public EmployeeController(EmployeeModelAssembler assembler, EmployeeMappingService employeeMappingService) {
        Assert.notNull(assembler, "assembler must not be null!");
        this.assembler = assembler;
        Assert.notNull(employeeMappingService, "employeeMappingService must not be null!");
        this.employeeMappingService = employeeMappingService;
    }

    //all employees
    @GetMapping("/employees")
    public CollectionModel<EntityModel<EmployeeDTO>> all() {
        List<EmployeeDTO> employeesDTO = employeeMappingService.getAllEmployees();
        List<EntityModel<EmployeeDTO>> employees = employeesDTO.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    //Single employee
    @GetMapping("/employees/{id}")
    public EntityModel<EmployeeDTO> singleEmployee(@PathVariable Long id) {
        EmployeeDTO employee = employeeMappingService.getSingleEmployee(id);

        return assembler.toModel(employee);
    }

    @PostMapping("/employees")
    public ResponseEntity<?> newEmployee(@RequestBody EmployeeDTO newEmployee) {
        EntityModel<EmployeeDTO> entityModel = assembler.toModel(employeeMappingService.save(newEmployee));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/employees/{id}")
    public EmployeeDTO replaceEmployee(@RequestBody EmployeeDTO newEmployee, @PathVariable Long id) {
        try {
            EmployeeDTO employeeDTO = employeeMappingService.getSingleEmployee(id);
            employeeDTO.setName(newEmployee.getName());
            employeeDTO.setRole(newEmployee.getRole());
            return employeeMappingService.save(employeeDTO);
        } catch (Exception e) {
            newEmployee.setId(id);
            return employeeMappingService.save(newEmployee);
        }
    }

    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeMappingService.deleteById(id);
    }
}
