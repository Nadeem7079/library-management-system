package com.library.librarysystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.library.librarysystem.entity.Role;
import com.library.librarysystem.entity.User;
import com.library.librarysystem.repository.UserRepository;
import com.library.librarysystem.security.JwtUtl;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtl jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;


    @Test
    void register_success() {

        User user = new User();
        user.setName("Hari");
        user.setEmail("hari@gmail.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        when(passwordEncoder.encode("password"))
                .thenReturn("encodedPassword");

        when(userRepository.save(user))
                .thenReturn(user);

        User result = authService.register(user);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());

        verify(passwordEncoder).encode("password");
        verify(userRepository).save(user);
    }


    @Test
    void login_success() {

        String email = "hari@gmail.com";
        String password = "password";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(jwtUtil.generateToken(email, "USER"))
                .thenReturn("test-jwt-token");

        String token = authService.login(email, password);

        assertNotNull(token);
        assertEquals("test-jwt-token", token);

        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );

        verify(jwtUtil).generateToken(email, "USER");
    }


    @Test
    void login_userNotFound_throwsException() {

        String email = "unknown@gmail.com";
        String password = "password";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> authService.login(email, password)
        );
    }
}