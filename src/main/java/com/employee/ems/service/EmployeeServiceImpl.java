package com.employee.ems.service;

import com.employee.ems.config.AppProperties;
import com.employee.ems.dto.EmployeeRequest;
import com.employee.ems.dto.EmployeeResponse;
import com.employee.ems.exception.DuplicateResourceException;
import com.employee.ems.exception.ResourceNotFoundException;
import com.employee.ems.mapper.EmployeeMapper;
import com.employee.ems.model.Department;
import com.employee.ems.model.Employee;
import com.employee.ems.model.Status;
import com.employee.ems.repository.EmployeeRepository;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;
    private final AppProperties properties;

    public EmployeeServiceImpl(EmployeeRepository repository, EmployeeMapper mapper, AppProperties properties) {
        this.repository = repository; this.mapper = mapper; this.properties = properties;
    }

    @Override public EmployeeResponse create(EmployeeRequest request) {
        String email = normalizeEmail(request.getEmail());
        if (repository.existsByEmail(email)) throw new DuplicateResourceException("Employee already exists with email " + email);
        if (request.getSalary().compareTo(properties.getMaxSalary()) > 0)
            throw new IllegalArgumentException("salary must not exceed configured maximum of " + properties.getMaxSalary());
        Employee employee = mapper.toNewEntity(request);
        if (employee.getDepartment() == null) employee.setDepartment(properties.getDefaultDepartment());
        Employee saved = repository.save(employee);
        log.info("Created employee id={} department={}", saved.getId(), saved.getDepartment());
        return mapper.toResponse(saved);
    }
    @Override public List<EmployeeResponse> findAll(Department department, Status status, String sortBy, String order) {
        Comparator<Employee> comparator = comparatorFor(sortBy);
        if ("desc".equalsIgnoreCase(order)) comparator = comparator.reversed();
        else if (!"asc".equalsIgnoreCase(order)) throw new IllegalArgumentException("order must be 'asc' or 'desc'");
        return repository.findAll().stream()
            .filter(e -> department == null || e.getDepartment() == department)
            .filter(e -> status == null || e.getStatus() == status)
            .sorted(comparator).map(mapper::toResponse).toList();
    }
    private Comparator<Employee> comparatorFor(String sortBy) {
        return switch (sortBy == null ? "id" : sortBy.toLowerCase(Locale.ROOT)) {
            case "id" -> Comparator.comparing(Employee::getId);
            case "firstname" -> Comparator.comparing(Employee::getFirstName, String.CASE_INSENSITIVE_ORDER);
            case "lastname" -> Comparator.comparing(Employee::getLastName, String.CASE_INSENSITIVE_ORDER);
            case "email" -> Comparator.comparing(Employee::getEmail, String.CASE_INSENSITIVE_ORDER);
            case "salary" -> Comparator.comparing(Employee::getSalary);
            case "dateofjoining" -> Comparator.comparing(Employee::getDateOfJoining);
            case "department" -> Comparator.comparing(e -> e.getDepartment().name());
            default -> throw new IllegalArgumentException("Unsupported sortBy field: " + sortBy);
        };
    }
    @Override public EmployeeResponse findById(Long id) { return mapper.toResponse(getOrThrow(id)); }
    @Override public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = getOrThrow(id);
        String email = normalizeEmail(request.getEmail());
        if (repository.existsByEmailAndIdNot(email, id))
            throw new DuplicateResourceException("Employee already exists with email " + email);
        if (request.getSalary().compareTo(properties.getMaxSalary()) > 0)
            throw new IllegalArgumentException("salary must not exceed configured maximum of " + properties.getMaxSalary());
        mapper.updateEntity(employee, request);
        Employee saved = repository.save(employee);
        log.info("Updated employee id={}", id);
        return mapper.toResponse(saved);
    }
    @Override public EmployeeResponse changeStatus(Long id, Status status) {
        if (status == null) throw new IllegalArgumentException("status is required and must be ACTIVE or INACTIVE");
        Employee employee = getOrThrow(id);
        employee.setStatus(status);
        repository.save(employee);
        log.info("Changed employee status id={} status={}", id, status);
        return mapper.toResponse(employee);
    }
    @Override public void delete(Long id) {
        getOrThrow(id);
        repository.deleteById(id);
        log.info("Permanently deleted employee id={}", id);
    }
    @Override public List<EmployeeResponse> search(String keyword) {
        if (keyword == null || keyword.isBlank()) throw new IllegalArgumentException("keyword must not be blank");
        String term = keyword.trim().toLowerCase(Locale.ROOT);
        return repository.findAll().stream()
            .filter(e -> e.getFirstName().toLowerCase(Locale.ROOT).contains(term)
                || e.getLastName().toLowerCase(Locale.ROOT).contains(term)
                || e.getEmail().toLowerCase(Locale.ROOT).contains(term))
            .map(mapper::toResponse).toList();
    }
    @Override public List<EmployeeResponse> findByDepartment(Department department) {
        return repository.findAll().stream().filter(e -> e.getDepartment() == department).map(mapper::toResponse).toList();
    }
    @Override public Map<String, Object> statistics() {
        List<Employee> all = repository.findAll();
        Map<String, Long> headcount = all.stream().collect(Collectors.groupingBy(
            e -> e.getDepartment().name(), Collectors.counting()));
        BigDecimal average = all.stream().map(Employee::getSalary).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (!all.isEmpty()) average = average.divide(BigDecimal.valueOf(all.size()), 2, java.math.RoundingMode.HALF_UP);
        return Map.of("totalEmployees", all.size(), "headcountByDepartment", headcount, "averageSalary", average);
    }
    private Employee getOrThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
    }
    private String normalizeEmail(String email) { return email.trim().toLowerCase(Locale.ROOT); }
}
