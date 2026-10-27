package com.employee.ems.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI employeeApi() {
        return new OpenAPI().info(new Info()
            .title("Employee Management REST API")
            .version("v1")
            .description("REST API for creating, searching, updating, and deactivating employees."));
    }
}
