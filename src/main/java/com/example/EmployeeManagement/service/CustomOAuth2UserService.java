package com.example.EmployeeManagement.service;

import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.EmployeeManagement.model.Role;
import com.example.EmployeeManagement.model.User;
import com.example.EmployeeManagement.repository.RoleRepository;
import com.example.EmployeeManagement.repository.UserRepository;

public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");


        User user = userRepository.findByEmail(email)
        .orElseGet(() -> {
            //Default role "Trainee" for new users.
            Role userRole = roleRepository.findByRole("Trainee")
                .orElseThrow(()->new RuntimeException("Default role not found"));

            //Creating new user with addded role
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(name);
            newUser.setRole(userRole);
            return userRepository.save(newUser);
        });

        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("Role_"+user.getRole().getRole())),
                oAuth2User.getAttributes(),
                "name"
        );
    }
}



