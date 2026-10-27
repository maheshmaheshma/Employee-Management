package com.employee.ems.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.employee.ems.dto.EmployeeRequest;
import com.employee.ems.dto.EmployeeResponse;
import com.employee.ems.exception.GlobalExceptionHandler;
import com.employee.ems.model.Department;
import com.employee.ems.model.Status;
import com.employee.ems.service.EmployeeService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean EmployeeService service;

    private EmployeeRequest validRequest() {
        EmployeeRequest r = new EmployeeRequest();
        r.setFirstName("Mahesh"); r.setLastName("Mamidi"); r.setEmail("mahesh@example.com");
        r.setPhone("9876543210"); r.setDepartment(Department.IT); r.setDesignation("Developer");
        r.setSalary(new BigDecimal("45000.00")); r.setDateOfJoining(LocalDate.of(2025,1,10));
        return r;
    }
    private EmployeeResponse response() {
        return new EmployeeResponse(1L,"Mahesh","Mamidi","mahesh@example.com","9876543210",
            Department.IT,"Developer",new BigDecimal("45000.00"),LocalDate.of(2025,1,10),Status.ACTIVE);
    }
    @Test void createReturns201() throws Exception {
        when(service.create(any())).thenReturn(response());
        mvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validRequest())))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(1)));
    }
    @Test void invalidInputReturns400() throws Exception {
        EmployeeRequest invalid = validRequest(); invalid.setEmail("not-an-email");
        mvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalid)))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.fieldErrors", not(empty())));
    }
    @Test void missingIdReturns404() throws Exception {
        when(service.findById(99L)).thenThrow(new com.employee.ems.exception.ResourceNotFoundException("Employee not found with id 99"));
        mvc.perform(get("/api/v1/employees/99"))
            .andExpect(status().isNotFound()).andExpect(jsonPath("$.message", containsString("99")));
    }
}
