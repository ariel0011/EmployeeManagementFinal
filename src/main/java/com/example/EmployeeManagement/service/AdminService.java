package com.example.EmployeeManagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.EmployeeManagement.dto.ResponseDTO;
import com.example.EmployeeManagement.dto.UserProjection;
import com.example.EmployeeManagement.dto.UsersListDTO;
import com.example.EmployeeManagement.model.Role;
import com.example.EmployeeManagement.model.User;
import com.example.EmployeeManagement.repository.RoleRepository;
import com.example.EmployeeManagement.repository.UserRepository;

@Service
public class AdminService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public AdminService( UserRepository userRepository, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    //METHOD TO CHANGE ROLE OF USER
    public ResponseDTO changeUserRole(String email, String roleName){

        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null) return new ResponseDTO(false,"No user found");

        Role role = roleRepository.findByRole(roleName).orElse(null);
        if(role == null) return new ResponseDTO(false, "Role doesn't exists");

        user.setRole(role);
        userRepository.save(user);
        return new ResponseDTO(true,"Role changed successfully");
    }

    //METHOD TO FETCH ALL USERS DATA
    public UsersListDTO fetchAllUsers(){
        List<UserProjection> result = userRepository.findBy();
        return new UsersListDTO(true, "Fetched all users successfully", result);
    }

}
