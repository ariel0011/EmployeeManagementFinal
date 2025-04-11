package com.example.EmployeeManagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginRequestDTO 
{
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8 , max = 21)
    private String password;

    public LoginRequestDTO(String email , String password){
        this.email = email;
        this.password = password;
    }
}
