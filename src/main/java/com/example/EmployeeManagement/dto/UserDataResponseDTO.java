package com.example.EmployeeManagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserDataResponseDTO extends ResponseDTO {
    public String username;
    public String email;
    public Integer age;
    
    public UserDataResponseDTO(boolean success , String message , String username, String email , Integer age) {
        super(success, message);
        this.username = username;
        this.email = email;
        this.age = age;
    }
}