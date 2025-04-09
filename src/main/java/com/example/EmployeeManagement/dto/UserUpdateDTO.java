package com.example.EmployeeManagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserUpdateDTO {

    @NotBlank(message = "Username can't be empty")
    @Size(min = 3, max = 25, message = "User name should be atleast 3 character long")
    private String username;
    
    @NotBlank(message = "Email can't be empty")
    @Email(message = "Invalid email address")
    private String email;

    @Min(18)
    @Max(80)
    private Integer age;
}
