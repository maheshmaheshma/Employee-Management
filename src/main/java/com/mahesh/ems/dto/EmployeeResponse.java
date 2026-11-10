package com.mahesh.ems.dto;

import com.mahesh.ems.model.Department;
import com.mahesh.ems.model.Status;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponse(Long id, String firstName, String lastName, String email, String phone,
                               Department department, String designation, BigDecimal salary,
                               LocalDate dateOfJoining, Status status) {}
