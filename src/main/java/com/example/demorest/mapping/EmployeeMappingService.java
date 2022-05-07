package com.example.demorest.mapping;

import com.example.demorest.dto.EmployeeDTO;
import com.example.demorest.entities.Employee;
import com.example.demorest.exceptions.EmployeeNotFoundException;
import com.example.demorest.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeMappingService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeMappingService(EmployeeRepository employeeRepository) {
        Assert.notNull(employeeRepository, "employeeRepository must not be null!");
        this.employeeRepository = employeeRepository;
    }

    //create getAllEmployees() method that returns a list of EmployeeDTO
    public List<EmployeeDTO> getAllEmployees() {
        return (employeeRepository
                .findAll())
                .stream()
                .map(this::convertEmployeeDataToDTO)
                .collect(Collectors.toList());
    }

    public EmployeeDTO getSingleEmployee(Long id) {
        return convertEmployeeDataToDTO(employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id)));
    }

    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.save(convertEmployeeDTOToEmployee(employeeDTO));

        return convertEmployeeDataToDTO(employee);
    }

    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    //create convertEmployeeDataIntoDTO method which returns EmployeeDTO
    private EmployeeDTO convertEmployeeDataToDTO(Employee employee) {
        //create instance of EmployeeDTO class
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setRole(employee.getRole());

        return employeeDTO;
    }

    //converts EmployeeDTO objects to Employee entities
    private Employee convertEmployeeDTOToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setName(employeeDTO.getName());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setRole(employeeDTO.getRole());

        return employee;
    }
}
