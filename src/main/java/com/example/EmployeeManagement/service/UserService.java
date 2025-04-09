package com.example.EmployeeManagement.service;
import java.time.LocalDateTime;
import java.util.*;
import com.example.EmployeeManagement.dto.ResponseDTO;
import com.example.EmployeeManagement.dto.UserRegisterDto;
import com.example.EmployeeManagement.dto.UserDataResponseDTO;
import com.example.EmployeeManagement.dto.UserUpdateDTO;
import com.example.EmployeeManagement.exception.ResourceNotFoundException;
import com.example.EmployeeManagement.model.Role;
import com.example.EmployeeManagement.model.User;
import com.example.EmployeeManagement.repository.RoleRepository;
import com.example.EmployeeManagement.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
    }

    //REGISTER USER METHOD
    @Transactional
    public ResponseDTO addUser(UserRegisterDto userRequestDTO) {

        boolean userNameExists = userRepository.existsByUsername(userRequestDTO.getUsername());
        boolean emailExists = userRepository.existsByEmail(userRequestDTO.getEmail());
    
        if (userNameExists || emailExists) {
            return new ResponseDTO(false, 
                    userNameExists ? "Username already exists" : "Email already exists");
        }
        Role userRole = roleRepository.findByRole("Trainee").orElse(null);
        if(userRole == null) return new ResponseDTO( false, "Default Role doesn't exist");

        User user = User.builder()
                .username(userRequestDTO.getUsername())
                .email(userRequestDTO.getEmail())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .age(userRequestDTO.getAge())
                .emailConfirmed(false)
                .role(userRole)
                .build();

        userRepository.save(user);
        ResponseDTO isGeneratedOtp = generateAndSetOtp(userRequestDTO.getEmail()); 
        return isGeneratedOtp;
    }

    //FETCH USER DETAILS METHOD
    public UserDataResponseDTO fetchUserData(Long id){
        var user = userRepository.findById(id);
        if(user.isEmpty()){
            return new UserDataResponseDTO(false,"User not found", "" , "" , null);
        }
        var userData = user.get();
        return new UserDataResponseDTO(true, "Fetched user data successfully", userData.getUsername(), userData.getEmail(), userData.getAge());
    }

    //DELETE USER METHOD
    @Transactional
    public ResponseDTO deleteUser(Long id){
        var user = userRepository.findById(id).orElse(null);
        
        if(user == null){
            throw new ResourceNotFoundException("User does not exists");
        }

        userRepository.delete(user);
        return new ResponseDTO(true, "User deleted successfully");
    }

    //UPDATE USER DATA METHOD
    @Transactional
    public ResponseDTO updateUserDetails(Long id , UserUpdateDTO userDataToUpdate){

        var user = userRepository.findById(id);
        if(user.isEmpty()) return new ResponseDTO(false, "User does not exist");

        //Check if username or email already exists
        var userNameExists = userRepository.existsByUsername(userDataToUpdate.getUsername());
        var emailExists = userRepository.existsByEmail(userDataToUpdate.getEmail());
        if(userNameExists || emailExists){
            return new ResponseDTO(false, userNameExists ? "User name already exists" :"Email already exists");
        }

        var userOldData = user.get();
        userOldData.setUsername(userDataToUpdate.getUsername());
        userOldData.setEmail(userDataToUpdate.getEmail());
        userOldData.setAge(userDataToUpdate.getAge());

        userRepository.save(userOldData);
        return new ResponseDTO(true, "User details updated successfully");
    }

    //METHOD TO CONFIRM OTP
    public ResponseDTO confirmOtp(String email, String otp){
        User user = userRepository.findByEmail(email).orElse(null);

        if(user == null){
            return new ResponseDTO(false,"User not found");
        }
        if(user.isEmailConfirmed()){
            return new ResponseDTO(true,"Email already confirmed");
        }
        if(!user.getOtp().equals(otp)){
            return new ResponseDTO(false, "Invalid otp");
        }
        if(user.getOtpGeneratedAt().isBefore(LocalDateTime.now().minusMinutes(10))){
            return new ResponseDTO(false,"OTP expired");
        }

        user.setEmailConfirmed(true);
        user.setOtp(null);
        user.setOtpGeneratedAt(null);
        userRepository.save(user);

        //welcome email to user after email confirmation
        // String subject = "Welcome email";
        // String message = "Hey there! Welcome to our application";
        // emailService.sendMail(user.getEmail(), subject, message);

        return new ResponseDTO(true,"Email confirmed successfully");
    } 

    //METHOD TO GENERATE AND SET OTP IN DB
    public ResponseDTO generateAndSetOtp(String email){

        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null){
            return new ResponseDTO(false,"User not found");
        }
        if(user.isEmailConfirmed()){
            return new ResponseDTO(true,"Email already confirmed");
        }

        String otp = otpGeneration();
        user.setOtp(otp);   
        user.setOtpGeneratedAt(LocalDateTime.now());

        String subject = "Confirm you email";
        String message = "Your otp is: " + otp;
        emailService.sendMail(email, subject, message);

        return new ResponseDTO(true, "Otp confirmation mail sent successfully");

    }
 
    //METHOD TO GENERATE RANDOM OTP
    private String otpGeneration(){
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

}