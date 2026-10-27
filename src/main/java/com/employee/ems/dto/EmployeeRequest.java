package com.employee.ems.dto;

import com.employee.ems.model.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Fields required to create or fully update an employee")
public class EmployeeRequest {
    @NotBlank @Size(min=2, max=50) @Pattern(regexp="^[\\p{L}]+(?:[ '-][\\p{L}]+)*$", message="must contain letters only")
    @Schema(example="Mahesh") private String firstName;
    @NotBlank @Size(min=2, max=50) @Pattern(regexp="^[\\p{L}]+(?:[ '-][\\p{L}]+)*$", message="must contain letters only")
    @Schema(example="Mamidi") private String lastName;
    @NotBlank @Email @Size(max=254) @Schema(example="mahesh@example.com") private String email;
    @NotBlank @Pattern(regexp="^[0-9]{10}$", message="must be a 10-digit number") @Schema(example="9876543210") private String phone;
    @NotNull @Schema(example="IT") private Department department;
    @NotBlank @Size(max=60) @Schema(example="Junior Software Engineer") private String designation;
    @NotNull @Positive @Digits(integer=10, fraction=2) @Schema(example="45000.00") private BigDecimal salary;
    @NotNull @PastOrPresent @Schema(example="2025-06-15") private LocalDate dateOfJoining;

    public String getFirstName(){return firstName;} public void setFirstName(String v){firstName=v;}
    public String getLastName(){return lastName;} public void setLastName(String v){lastName=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public Department getDepartment(){return department;} public void setDepartment(Department v){department=v;}
    public String getDesignation(){return designation;} public void setDesignation(String v){designation=v;}
    public BigDecimal getSalary(){return salary;} public void setSalary(BigDecimal v){salary=v;}
    public LocalDate getDateOfJoining(){return dateOfJoining;} public void setDateOfJoining(LocalDate v){dateOfJoining=v;}
}
