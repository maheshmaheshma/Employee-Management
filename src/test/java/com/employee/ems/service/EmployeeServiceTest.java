package com.employee.ems.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.employee.ems.config.AppProperties;
import com.employee.ems.dto.EmployeeRequest;
import com.employee.ems.exception.DuplicateResourceException;
import com.employee.ems.exception.ResourceNotFoundException;
import com.employee.ems.mapper.EmployeeMapper;
import com.employee.ems.model.Department;
import com.employee.ems.model.Employee;
import com.employee.ems.model.Status;
import com.employee.ems.repository.EmployeeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock EmployeeRepository repository;
    EmployeeServiceImpl service;

    @BeforeEach void setUp() {
        AppProperties props = new AppProperties();
        service = new EmployeeServiceImpl(repository, new EmployeeMapper(), props);
    }
    private EmployeeRequest request(String email) {
        EmployeeRequest r = new EmployeeRequest();
        r.setFirstName("Mahesh"); r.setLastName("Mamidi"); r.setEmail(email);
        r.setPhone("9876543210"); r.setDepartment(Department.IT); r.setDesignation("Developer");
        r.setSalary(new BigDecimal("45000.00")); r.setDateOfJoining(LocalDate.of(2025, 1, 10));
        return r;
    }
    private Employee employee(long id, String email) {
        return new Employee(id, "Mahesh", "Mamidi", email, "9876543210", Department.IT,
            "Developer", new BigDecimal("45000.00"), LocalDate.of(2025,1,10), Status.ACTIVE);
    }
    @Test void createEmployee() {
        when(repository.existsByEmail("mahesh@example.com")).thenReturn(false);
        when(repository.save(any())).thenAnswer(i -> { Employee e=i.getArgument(0); e.setId(1L); return e; });
        assertEquals(1L, service.create(request("Mahesh@example.com")).id());
    }
    @Test void duplicateEmailIsRejected() {
        when(repository.existsByEmail("mahesh@example.com")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> service.create(request("Mahesh@example.com")));
    }
    @Test void getMissingEmployeeThrowsNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }
    @Test void updateEmployee() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee(1L, "old@example.com")));
        when(repository.existsByEmailAndIdNot("new@example.com", 1L)).thenReturn(false);
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));
        assertEquals("new@example.com", service.update(1L, request("new@example.com")).email());
    }
    @Test void deactivateEmployee() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee(1L, "a@example.com")));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));
        assertEquals(Status.INACTIVE, service.changeStatus(1L, Status.INACTIVE).status());
    }
    @Test void searchMatchesEmail() {
        when(repository.findAll()).thenReturn(java.util.List.of(employee(1L, "mahesh@example.com")));
        assertEquals(1, service.search("MAHESH@").size());
    }
}
