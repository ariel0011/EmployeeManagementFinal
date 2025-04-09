package com.example.EmployeeManagement.controller;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.EmployeeManagement.dto.OtpRequestDTO;
import com.example.EmployeeManagement.dto.ResponseDTO;
import com.example.EmployeeManagement.dto.UserRegisterDto;
import com.example.EmployeeManagement.service.UserDetailsServiceImpl;
import com.example.EmployeeManagement.service.UserService;
import com.example.EmployeeManagement.utils.JwtUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/Auth")
@Tag(name = "Auth Management", description = "APIs for user login signup")
@Slf4j
public class AuthController {
    
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService ,UserDetailsServiceImpl userDetailsServiceImpl, AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }  
    
    //METHOD TO EXTRACT VALIDATION ERRORS
    private String extractedValidationErrors(BindingResult result) {
        return result.getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
    }


    //USER REGISTER API
    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserRegisterDto userData , BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = extractedValidationErrors(result);
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(false, errorMessage));
        }

        try {
            var response = userService.addUser(userData);
    
            return response.isSuccess()
                    ? ResponseEntity.ok(response)
                    : ResponseEntity.badRequest().body(response);
        } 
        catch (DataIntegrityViolationException ex) {
            log.error("Database constraint violation while registering user: {}", ex.getMessage(), ex);
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(false, "Database constraint violation occurred"));
        } 
        catch (Exception ex) {
            log.error("Unexpected error while registering user", ex);
            return ResponseEntity.internalServerError()
                    .body(new ResponseDTO(false, "Internal server error occurred"));
        }
    }


    //OTP CONFIRMATION API
    @PostMapping("/otp/confirm")
    public ResponseEntity<ResponseDTO> confirmOtp(@RequestBody @Valid OtpRequestDTO request, BindingResult result) {
        
        if(result.hasErrors()){
            if (result.hasErrors()) {
                String errorMessage = extractedValidationErrors(result);
                return ResponseEntity.badRequest()
                        .body(new ResponseDTO(false, errorMessage));
            }
        }
        try{

            var response = userService.confirmOtp(request.getEmail(),request.getOtp());
    
            return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
        }
        catch(Exception ex){
            log.error("Unexpected error while confirming OTP", ex);
            return ResponseEntity.internalServerError()
                    .body(new ResponseDTO(false, "Internal server error occurred"));
        }
    }


    //OTP RESEND API
    @PostMapping("/otp/resend")
    public ResponseEntity<ResponseDTO> resendOtp(@RequestBody String email) {
        
        if(email.isEmpty()){
            return ResponseEntity.badRequest().body(new ResponseDTO(false, "Email is required"));
        }
        try{

            var response = userService.generateAndSetOtp(email);
            return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
        }
        catch(Exception ex){
            log.error("Unexpected error while resending OTP", ex);
            return ResponseEntity.internalServerError()
                    .body(new ResponseDTO(false, "Internal server error occurred"));
        }
    }
    

    //USER LOGIN API
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        if(username.isEmpty()|| password.isEmpty()){
            return ResponseEntity.badRequest().body(new ResponseDTO(false, "username or password is missing")); 
        }

        try {

            //Authenticate user
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username , password)
            );
            //Load user details
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
            //Fetch user role
            String role = userDetails.getAuthorities().stream()
                            .findFirst()
                            .map(auth->auth.getAuthority())
                            .orElse("Role_Trainee");
            //Create JWT token
            String token = jwtUtil.generateToken(username , role);
            return ResponseEntity.ok().body(token);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO(false, "Invalid username or password"));
        } catch (Exception e) {
            log.error("Error while login user", e);
            return ResponseEntity.internalServerError()
                    .body(new ResponseDTO(false, "Login failed"));
        }
    }

    //USER LOGOUT API
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        response.sendRedirect("https://accounts.google.com/Logout");
    }
}
