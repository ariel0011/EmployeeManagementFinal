package com.example.EmployeeManagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ResponseDTO {
    private boolean success;
    private String message;
    
    public ResponseDTO(boolean success , String message){
        this.success = success;
        this.message = message;
}
}
