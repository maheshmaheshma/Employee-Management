package com.employee.ems.service;

import com.employee.ems.dto.EmployeeRequest;
import com.employee.ems.dto.EmployeeResponse;
import com.employee.ems.model.Department;
import com.employee.ems.model.Status;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface EmployeeService {
    EmployeeResponse create(EmployeeRequest request);
    List<EmployeeResponse> findAll(Department department, Status status, String sortBy, String order);
    EmployeeResponse findById(Long id);
    EmployeeResponse update(Long id, EmployeeRequest request);
    EmployeeResponse changeStatus(Long id, Status status);
    void delete(Long id);
    List<EmployeeResponse> search(String keyword);
    List<EmployeeResponse> findByDepartment(Department department);
    Map<String, Object> statistics();
}
