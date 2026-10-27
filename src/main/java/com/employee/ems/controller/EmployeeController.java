package com.employee.ems.controller;

import com.employee.ems.dto.EmployeeRequest;
import com.employee.ems.dto.EmployeeResponse;
import com.employee.ems.model.Department;
import com.employee.ems.model.Status;
import com.employee.ems.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employees", description = "Employee lifecycle and search operations")
public class EmployeeController {
    private final EmployeeService service;
    public EmployeeController(EmployeeService service) { this.service = service; }

    @PostMapping
    @Operation(summary = "Create an employee", description = "Creates an employee and returns HTTP 201.")
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    @Operation(summary = "List employees", description = "Optional filters and sorting are applied in the service layer.")
    public List<EmployeeResponse> findAll(
        @RequestParam(required=false) Department department,
        @RequestParam(required=false) Status status,
        @RequestParam(defaultValue="id") String sortBy,
        @RequestParam(defaultValue="asc") String order) {
        return service.findAll(department, status, sortBy, order);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID")
    public EmployeeResponse findById(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/{id}")
    @Operation(summary = "Fully update an employee")
    public EmployeeResponse update(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Activate or deactivate an employee")
    public EmployeeResponse changeStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String raw = body == null ? null : body.get("status");
        try { return service.changeStatus(id, raw == null ? null : Status.valueOf(raw.toUpperCase())); }
        catch (IllegalArgumentException ex) { throw new IllegalArgumentException("status must be ACTIVE or INACTIVE"); }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Permanently delete an employee")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search employees by first name, last name, or email")
    public List<EmployeeResponse> search(@RequestParam @Parameter(description="Text to search for") String keyword) {
        return service.search(keyword);
    }

    @GetMapping("/department/{dept}")
    @Operation(summary = "List employees by department")
    public List<EmployeeResponse> findByDepartment(@PathVariable Department dept) {
        return service.findByDepartment(dept);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get headcount per department and average salary")
    public Map<String, Object> statistics() { return service.statistics(); }
}
