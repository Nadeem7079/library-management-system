package com.library.librarysystem.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.library.librarysystem.entity.User;
import com.library.librarysystem.repository.UserRepository;
import com.library.librarysystem.entity.Status;
import com.library.librarysystem.repository.ReservationRepository;
import com.library.librarysystem.exception.ResourceNotFoundException;
import com.library.librarysystem.exception.ActiveUserReservationException;

@Service
public class UserService {

    private final UserRepository userRepository;
    
    private final ReservationRepository reservationRepository;

    public UserService(UserRepository userRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    // Add User
    public User addUser(User user) {
        return userRepository.save(user);
    }

    // Get All Users
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // Get User By Email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        )
                );
    }
    
    // Delete User
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        boolean hasActiveReservations =
                reservationRepository.existsByUserIdAndStatus(
                        userId, Status.ACTIVE);

        if (hasActiveReservations) {
            throw new ActiveUserReservationException(
                "User cannot be deleted because they still have active reservations"
            );
        }

        userRepository.delete(user);
    }
    
}