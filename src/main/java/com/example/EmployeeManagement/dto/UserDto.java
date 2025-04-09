package com.example.EmployeeManagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserDto{
    public String username;
    public String email;
    public Integer age;
    
    public UserDto(String username, String email , Integer age) {
        this.username = username;
        this.email = email;
        this.age = age;
    }
}
