package com.employee.ems.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Department department;
    private String designation;
    private BigDecimal salary;
    private LocalDate dateOfJoining;
    private Status status;

    public Employee() {}
    public Employee(Long id, String firstName, String lastName, String email, String phone,
                    Department department, String designation, BigDecimal salary,
                    LocalDate dateOfJoining, Status status) {
        this.id=id; this.firstName=firstName; this.lastName=lastName; this.email=email;
        this.phone=phone; this.department=department; this.designation=designation;
        this.salary=salary; this.dateOfJoining=dateOfJoining; this.status=status;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id=id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName=firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName=lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email=email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone=phone; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department=department; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation=designation; }
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary=salary; }
    public LocalDate getDateOfJoining() { return dateOfJoining; }
    public void setDateOfJoining(LocalDate dateOfJoining) { this.dateOfJoining=dateOfJoining; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status=status; }
}
