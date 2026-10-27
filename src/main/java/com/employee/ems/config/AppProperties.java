package com.employee.ems.config;

import com.employee.ems.model.Department;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    @NotBlank private String name = "Employee Management REST API";
    @NotNull private Department defaultDepartment = Department.HR;
    @NotNull @DecimalMin(value = "0.0", inclusive = false) private BigDecimal maxSalary = new BigDecimal("10000000");

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Department getDefaultDepartment() { return defaultDepartment; }
    public void setDefaultDepartment(Department defaultDepartment) { this.defaultDepartment = defaultDepartment; }
    public BigDecimal getMaxSalary() { return maxSalary; }
    public void setMaxSalary(BigDecimal maxSalary) { this.maxSalary = maxSalary; }
}
