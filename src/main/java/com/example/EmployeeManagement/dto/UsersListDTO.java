package com.example.EmployeeManagement.dto;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class UsersListDTO extends ResponseDTO{
    
    private List<UserProjection> users;
    
    public UsersListDTO (boolean success , String message , List<UserProjection> users){
        super(success, message);
        this.users = users;
    }
}