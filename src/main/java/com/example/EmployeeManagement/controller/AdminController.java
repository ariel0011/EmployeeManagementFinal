package com.example.EmployeeManagement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.EmployeeManagement.dto.ResponseDTO;
import com.example.EmployeeManagement.service.AdminService;
import com.example.EmployeeManagement.service.EmailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/Admin")
@Tag(name = "Admin controller" , description = "APIs for admins")
@Slf4j
public class AdminController {
    
    private final EmailService emailService;
    private final AdminService adminService;

    public AdminController(EmailService emailService, AdminService adminService){
        this.emailService = emailService;
        this.adminService = adminService;
    }

    //API TO SEND WELCOME EMAIL TO USER
    @PostMapping("/sendWelcomeEmail")
    @Operation(summary = "API to send welcome email" , description = "Sends welcome email to ")
    public ResponseEntity<ResponseDTO> postMethodName(@RequestBody Map<String, String> payload) {

        String toEmail = payload.get("toEmail");
        String subject = payload.get("subject");
        String message = payload.get("message");

        if( toEmail.isEmpty() || message.isEmpty() || subject.isEmpty()){
            return ResponseEntity.badRequest().body(new ResponseDTO(false, "All fields are required"));
        }

        emailService.sendMail(toEmail, subject, message);
        return ResponseEntity.ok( new ResponseDTO(true, "Email sent."));
    }


    //API TO CHANGE USER ROLE
    @PostMapping("/changeUserRole")
    public ResponseEntity<ResponseDTO> changeUserRole(@RequestBody Map<String, String> requestData) {

        String userName = requestData.get("username");
        String role = requestData.get("role");
        if(userName.isEmpty() || role.isEmpty()) return ResponseEntity.badRequest().body( new ResponseDTO(false,"Username or role can't be empty"));
        try{
            var result = adminService.changeUserRole(userName,role);
            return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest()
                .body(result);
        }
        catch(Exception ex){
            log.error("Internal error occurred while changing user role.", ex);
            return ResponseEntity.internalServerError()
            .body(new ResponseDTO(false, "Internal error occured"));
        }
    }

    //API TO FETCH ALL USERS DATA
    @GetMapping("/allUsers")
    @Operation(summary = "Fetch all users" , description = "Fetched all users data")
    public ResponseEntity<?> fetchAllUsersDetails() {
        try{
            var result = adminService.fetchAllUsers();
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
    
}
