package com.library.librarysystem.controller;
import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.library.librarysystem.entity.User;
import com.library.librarysystem.service.AuthService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	

	
	@PostMapping("/register")
	public User registerUser(@Valid @RequestBody User user){
		
		return authService.register(user);
		
	}
	
	@PostMapping("/login")
	public String login(@RequestBody User loginRequest) {
		
		return authService.login(loginRequest.getEmail(), loginRequest.getPassword());
		
	}

}
