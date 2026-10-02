package com.library.librarysystem.controller;

import java.util.List;



import org.springframework.web.bind.annotation.*;

import com.library.librarysystem.entity.User;
import com.library.librarysystem.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Add User
 // Add User - ADMIN only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    // Get All Users - ADMIN only
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    // Get User By Email - USER or ADMIN
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
    	userService.deleteUser(id);
    }
}