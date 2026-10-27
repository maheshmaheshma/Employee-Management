package com.employee.ems.repository;

import com.employee.ems.model.Employee;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryEmployeeRepository implements EmployeeRepository {
    private final ConcurrentMap<Long, Employee> employees = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override public Employee save(Employee employee) {
        if (employee.getId() == null) employee.setId(sequence.incrementAndGet());
        employees.put(employee.getId(), employee);
        return employee;
    }
    @Override public Optional<Employee> findById(Long id) { return Optional.ofNullable(employees.get(id)); }
    @Override public List<Employee> findAll() {
        return employees.values().stream().sorted(Comparator.comparing(Employee::getId)).toList();
    }
    @Override public boolean existsByEmail(String email) {
        return employees.values().stream().anyMatch(e -> e.getEmail().equalsIgnoreCase(email));
    }
    @Override public boolean existsByEmailAndIdNot(String email, Long id) {
        return employees.values().stream().anyMatch(e -> !e.getId().equals(id) && e.getEmail().equalsIgnoreCase(email));
    }
    @Override public boolean deleteById(Long id) { return employees.remove(id) != null; }
}
