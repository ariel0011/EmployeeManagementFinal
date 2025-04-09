package com.example.EmployeeManagement.controller;

import com.example.EmployeeManagement.dto.*;
import com.example.EmployeeManagement.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/User")
@Tag(name = "User Management", description = "APIs for managing users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    //Private method to extract validation errors into a string
    private String extractedValidationErrors(BindingResult result) {
    return result.getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));
    }

    //API TO FETCH USER DATA
    @GetMapping("/{id}")
    @Operation(summary = "Fetch user data" , description = "Fetches user all details by provided ID")
    public ResponseEntity<?> GetUserData(@PathVariable Long id) {
        if(id == null || id<=0){
            return ResponseEntity.badRequest()
            .body(new ResponseDTO(false,"Invalid user id"));
        }
        try{
            var result = userService.fetchUserData(id);
            return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest()
                .body(result);
        }
        catch(Exception ex){
            log.error("Internal error occurred while registering user.", ex);
            return ResponseEntity.internalServerError()
            .body(new ResponseDTO(false, "Internal error occured"));
        }
    }
    
    //DELETE USER API
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete user details by ID")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable Long id){
        if(id == null || id<=0){
            return ResponseEntity.badRequest()
            .body(new ResponseDTO(false, "Invalid user id"));
        }

        try{
            ResponseDTO result = userService.deleteUser(id);
            return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
        }
        catch(Exception ex){
            log.error("Internal error occurred while deleting user.", ex);
            return ResponseEntity.internalServerError()
            .body(new ResponseDTO(false, "Internal error occured"));
        }
    }
    
    //API TO UPDATE USER DATA
    @PutMapping("/{id}")
    @Operation(summary = "Update user data")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO userData , BindingResult result) {
        if(result.hasErrors()){
            String errorMessage = extractedValidationErrors(result);
            return ResponseEntity.badRequest()
            .body(new ResponseDTO(false, errorMessage));
        }
        if(id == null || id<=0){
            return ResponseEntity.badRequest()
            .body(new ResponseDTO(false, "Invalid user id"));
        }

        try{
            var response = userService.updateUserDetails(id, userData);
            return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest()
                .body(response);
        }
        catch(Exception ex){
            log.error("Internal error occurred while registering user.", ex);
            return ResponseEntity.internalServerError()
            .body(new ResponseDTO(false, "Internal error occured"));
        }
    }
}
