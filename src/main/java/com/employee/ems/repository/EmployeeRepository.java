package com.employee.ems.repository;

import com.employee.ems.model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    Employee save(Employee employee);
    Optional<Employee> findById(Long id);
    List<Employee> findAll();
    boolean existsByEmail(String normalizedEmail);
    boolean existsByEmailAndIdNot(String normalizedEmail, Long id);
    boolean deleteById(Long id);
}
