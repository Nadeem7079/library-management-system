package com.library.librarysystem.service;

import java.util.*;

import com.library.librarysystem.entity.User;
import com.library.librarysystem.repository.UserRepository;
import com.library.librarysystem.security.JwtUtl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.library.librarysystem.exception.ResourceNotFoundException;
import com.library.librarysystem.exception.DuplicateResourceException;
@Service
public class AuthService {
	
	@Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtl jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    
    
 // Register user
    public User register(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException(
                    "User with email " + user.getEmail() + " already exists"
            );
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
    

    public String login(String email, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        
        User user = userRepository.findByEmail(email)
        .orElseThrow(() ->
                new ResourceNotFoundException(
                        "User not found with email: " + email
                )
        );
        
        System.out.println("User Role:" + user.getRole());

       
        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }
  }



