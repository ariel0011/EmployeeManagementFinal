package com.example.EmployeeManagement.model;
// import java.util.List;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , unique = true)
    private String username;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false , unique = true)
    private String email;

    @Column(nullable = true )
    private Integer age;
    
    @Column(nullable = true)
    private boolean emailConfirmed;

    @Column(nullable = true)
    private String  otp;

    @Column(nullable = true)
    private LocalDateTime otpGeneratedAt;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id") 
    private Role role;
}