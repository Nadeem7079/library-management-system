package com.library.librarysystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.library.librarysystem.entity.Status;
import com.library.librarysystem.entity.User;
import com.library.librarysystem.exception.ActiveUserReservationException;
import com.library.librarysystem.exception.ResourceNotFoundException;
import com.library.librarysystem.repository.ReservationRepository;
import com.library.librarysystem.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User();

        user.setId(1L);
        user.setName("Hari");
        user.setEmail("hari@gmail.com");
        user.setPassword("password");
    }

    // =========================================================
    // ADD USER
    // =========================================================

    @Test
    void addUser_success() {

        when(userRepository.save(user))
                .thenReturn(user);

        User result =
                userService.addUser(user);

        assertNotNull(result);
        assertEquals("Hari", result.getName());
        assertEquals("hari@gmail.com", result.getEmail());

        verify(userRepository)
                .save(user);
    }

    // =========================================================
    // GET ALL USERS
    // =========================================================

    @Test
    void getUsers_success() {

        User secondUser = new User();

        secondUser.setId(2L);
        secondUser.setName("Rahul");
        secondUser.setEmail("rahul@gmail.com");
        secondUser.setPassword("password");

        when(userRepository.findAll())
                .thenReturn(List.of(user, secondUser));

        List<User> result =
                userService.getUsers();

        assertNotNull(result);

        assertEquals(
                2,
                result.size()
        );

        assertEquals(
                "Hari",
                result.get(0).getName()
        );

        assertEquals(
                "Rahul",
                result.get(1).getName()
        );

        verify(userRepository)
                .findAll();
    }

    // =========================================================
    // GET USER BY EMAIL
    // =========================================================

    @Test
    void getUserByEmail_success() {

        when(userRepository.findByEmail("hari@gmail.com"))
                .thenReturn(Optional.of(user));

        User result =
                userService.getUserByEmail(
                        "hari@gmail.com"
                );

        assertNotNull(result);

        assertEquals(
                "hari@gmail.com",
                result.getEmail()
        );

        assertEquals(
                "Hari",
                result.getName()
        );

        verify(userRepository)
                .findByEmail("hari@gmail.com");
    }

    @Test
    void getUserByEmail_notFound_throwsException() {

        when(userRepository.findByEmail(
                "unknown@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserByEmail(
                        "unknown@gmail.com"
                )
        );

        verify(userRepository)
                .findByEmail("unknown@gmail.com");
    }

    // =========================================================
    // DELETE USER
    // =========================================================

    @Test
    void deleteUser_success() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(
                reservationRepository
                        .existsByUserIdAndStatus(
                                1L,
                                Status.ACTIVE
                        )
        ).thenReturn(false);

        userService.deleteUser(1L);

        verify(userRepository)
                .delete(user);
    }

    @Test
    void deleteUser_notFound_throwsException() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.deleteUser(999L)
        );

        verify(userRepository, never())
                .delete(any(User.class));

        verify(
                reservationRepository,
                never()
        ).existsByUserIdAndStatus(
                anyLong(),
                any(Status.class)
        );
    }

    @Test
    void deleteUser_activeReservation_throwsException() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(
                reservationRepository
                        .existsByUserIdAndStatus(
                                1L,
                                Status.ACTIVE
                        )
        ).thenReturn(true);

        assertThrows(
                ActiveUserReservationException.class,
                () -> userService.deleteUser(1L)
        );

        verify(userRepository, never())
                .delete(any(User.class));
    }
}