package com.library.librarysystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.library.librarysystem.entity.Book;
import com.library.librarysystem.entity.Reservation;
import com.library.librarysystem.entity.Status;
import com.library.librarysystem.entity.User;
import com.library.librarysystem.exception.BookNotAvailableException;
import com.library.librarysystem.exception.ReservationAlreadyReturnedException;
import com.library.librarysystem.exception.ResourceNotFoundException;
import com.library.librarysystem.repository.BookRepository;
import com.library.librarysystem.repository.ReservationRepository;
import com.library.librarysystem.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;




@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReservationService reservationService;

    private User user;
    private Book book;
    private Reservation reservation;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setName("Hari");
        user.setEmail("hari@gmail.com");

        book = new Book();
        book.setId(2L);
        book.setTitle("Java Basics");
        book.setAuthor("James");
        book.setIsbn("1234567890");
        book.setAvailableCopies(3);
        book.setIsAvailable(true);
        book.setDeleted(false);

        reservation = new Reservation();
        reservation.setId(10L);
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setIssueDate(LocalDate.now());
        reservation.setStatus(Status.ACTIVE);
    }

    // ---------------------------------------------------------
    // RESERVE BOOK TESTS
    // ---------------------------------------------------------

    @Test
    void reserveBook_success() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookRepository.findById(2L))
                .thenReturn(Optional.of(book));

        when(reservationRepository.save(any(Reservation.class)))
                .thenReturn(reservation);

        Reservation result =
                reservationService.reserveBook(1L, 2L);

        assertNotNull(result);

        assertEquals(Status.ACTIVE, result.getStatus());

        assertEquals(2, book.getAvailableCopies());

        assertTrue(book.isAvailable());

        verify(bookRepository).save(book);

        verify(reservationRepository)
                .save(any(Reservation.class));
    }

    @Test
    void reserveBook_userNotFound_throwsException() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservationService.reserveBook(999L, 2L)
        );

        verify(bookRepository, never())
                .save(any(Book.class));

        verify(reservationRepository, never())
                .save(any(Reservation.class));
    }

    @Test
    void reserveBook_bookNotFound_throwsException() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservationService.reserveBook(1L, 999L)
        );

        verify(bookRepository, never())
                .save(any(Book.class));

        verify(reservationRepository, never())
                .save(any(Reservation.class));
    }

    @Test
    void reserveBook_bookNotAvailable_throwsException() {

        book.setAvailableCopies(0);
        book.setIsAvailable(false);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookRepository.findById(2L))
                .thenReturn(Optional.of(book));

        assertThrows(
                BookNotAvailableException.class,
                () -> reservationService.reserveBook(1L, 2L)
        );

        verify(bookRepository, never())
                .save(any(Book.class));

        verify(reservationRepository, never())
                .save(any(Reservation.class));
    }

    // ---------------------------------------------------------
    // RETURN BOOK TESTS
    // ---------------------------------------------------------

    @Test
    void returnBook_success() {

        when(reservationRepository.findById(10L))
                .thenReturn(Optional.of(reservation));

        when(bookRepository.save(book))
                .thenReturn(book);

        when(reservationRepository.save(reservation))
                .thenReturn(reservation);

        when(authentication.getName())
        .thenReturn("hari@gmail.com");

doReturn(
        List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        )
).when(authentication).getAuthorities();

int copiesBeforeReturn =
        book.getAvailableCopies();

Reservation result =
        reservationService.returnBook(
                10L,
                authentication
        );

assertNotNull(result);

assertEquals(
        Status.RETURNED,
        result.getStatus()
);

assertNotNull(
        result.getReturnDate()
);

assertEquals(
        copiesBeforeReturn + 1,
        book.getAvailableCopies()
);

assertTrue(book.isAvailable());

verify(bookRepository)
        .save(book);

verify(reservationRepository)
        .save(reservation);    }
    
    
    

    @Test
    void returnBook_notFound_throwsException() {

        when(reservationRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservationService.returnBook(
                        999L,
                        authentication
                )
        );

        verify(bookRepository, never())
                .save(any(Book.class));

        verify(reservationRepository, never())
                .save(any(Reservation.class));
    }

    @Test
    void returnBook_alreadyReturned_throwsException() {

        reservation.setStatus(Status.RETURNED);
        reservation.setReturnDate(LocalDate.now());

        when(reservationRepository.findById(10L))
                .thenReturn(Optional.of(reservation));

        assertThrows(
                ReservationAlreadyReturnedException.class,
                () -> reservationService.returnBook(
                        10L,
                        authentication
                )
        );

        verify(bookRepository, never())
                .save(any(Book.class));

        verify(reservationRepository, never())
                .save(any(Reservation.class));
    }
}