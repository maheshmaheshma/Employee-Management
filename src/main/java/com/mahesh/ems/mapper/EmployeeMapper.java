package com.mahesh.ems.mapper;

import com.mahesh.ems.dto.EmployeeRequest;
import com.mahesh.ems.dto.EmployeeResponse;
import com.mahesh.ems.model.Employee;
import com.mahesh.ems.model.Status;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public Employee toNewEntity(EmployeeRequest request) {
        return new Employee(null, request.getFirstName().trim(), request.getLastName().trim(),
            request.getEmail().trim().toLowerCase(), request.getPhone(), request.getDepartment(),
            request.getDesignation().trim(), request.getSalary(), request.getDateOfJoining(), Status.ACTIVE);
    }
    public void updateEntity(Employee employee, EmployeeRequest request) {
        employee.setFirstName(request.getFirstName().trim());
        employee.setLastName(request.getLastName().trim());
        employee.setEmail(request.getEmail().trim().toLowerCase());
        employee.setPhone(request.getPhone());
        employee.setDepartment(request.getDepartment());
        employee.setDesignation(request.getDesignation().trim());
        employee.setSalary(request.getSalary());
        employee.setDateOfJoining(request.getDateOfJoining());
    }
    public EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(employee.getId(), employee.getFirstName(), employee.getLastName(),
            employee.getEmail(), employee.getPhone(), employee.getDepartment(), employee.getDesignation(),
            employee.getSalary(), employee.getDateOfJoining(), employee.getStatus());
    }
}
