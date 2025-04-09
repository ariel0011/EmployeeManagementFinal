package com.example.EmployeeManagement.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(
    title = "Employee management system",
    description = "API to manage employee",
    contact = @Contact(name = "API support", email = "Support@gmail.com" )
))
public class SwaggerConfig {
    
}
