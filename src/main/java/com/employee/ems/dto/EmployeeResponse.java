package com.employee.ems.dto;

import com.employee.ems.model.Department;
import com.employee.ems.model.Status;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponse(Long id, String firstName, String lastName, String email, String phone,
                               Department department, String designation, BigDecimal salary,
                               LocalDate dateOfJoining, Status status) {}
