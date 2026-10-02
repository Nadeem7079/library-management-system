package com.library.librarysystem.service;

import com.library.librarysystem.entity.*;

import com.library.librarysystem.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.*;
import com.library.librarysystem.exception.ResourceNotFoundException;
import com.library.librarysystem.exception.BookNotAvailableException;
import com.library.librarysystem.exception.ReservationAlreadyReturnedException;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;


@Service
public class ReservationService {
	

	private final ReservationRepository reservationRepository;
	
	private final  UserRepository userRepository;

	private final  BookRepository bookRepository;
	
	public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, BookRepository bookRepository  ) {
		
		this.bookRepository = bookRepository;
		this.reservationRepository = reservationRepository;
		this.userRepository = userRepository;
	}
	
	public Reservation reserveBook(Long userId, Long bookId) {
		User user = userRepository.findById(userId)
		        .orElseThrow(() ->
		                new ResourceNotFoundException(
		                        "User not found with id: " + userId
		                )
		        );
		Book book = bookRepository.findById(bookId)
		        .orElseThrow(() ->
		                new ResourceNotFoundException(
		                        "Book not found with id: " + bookId
		                )
		        );
		if (book.getAvailableCopies() <= 0) {
		    throw new BookNotAvailableException(
		            "Book is not available for reservation"
		    );
		}

	    Reservation reservation = new Reservation();
	    reservation.setUser(user);
	    reservation.setBook(book);
	    reservation.setIssueDate(LocalDate.now());
	    reservation.setStatus(Status.ACTIVE);

	    book.setAvailableCopies(book.getAvailableCopies() - 1);

	    if (book.getAvailableCopies() == 0) {
	        book.setIsAvailable(false);
	    }

	    bookRepository.save(book);

	    return reservationRepository.save(reservation);
	}
	
	public List<Reservation> getAllReservations(){
		return reservationRepository.findAll();
		
	}
	
	
	public List<Reservation> getReservationByUser(
	        Long userId,
	        String loggedInEmail,
	        Collection<? extends GrantedAuthority> authorities) {

	    User user = userRepository.findById(userId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "User not found with id: " + userId
	                    )
	            );

	    boolean isAdmin = authorities.stream()
	            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

	    if (!isAdmin && !user.getEmail().equals(loggedInEmail)) {
	        throw new org.springframework.security.access.AccessDeniedException(
	                "You can only view your own reservations"
	        );
	    }

	    return reservationRepository.findByUserId(userId);
	}
	
	
	public Reservation returnBook(
	        Long reservationId,
	        Authentication authentication) {

	    Reservation reservation = reservationRepository.findById(reservationId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Reservation not found with id: " + reservationId
	                    )
	            );

	    if (reservation.getStatus() == Status.RETURNED) {
	        throw new ReservationAlreadyReturnedException(
	                "Reservation with id " + reservationId
	                        + " has already been returned"
	        );
	    }

	    boolean isAdmin = authentication.getAuthorities()
	            .stream()
	            .anyMatch(authority ->
	                    authority.getAuthority().equals("ROLE_ADMIN"));

	    if (!isAdmin &&
	            !reservation.getUser().getEmail()
	                    .equals(authentication.getName())) {

	        throw new AccessDeniedException(
	                "You can only return your own reservation"
	        );
	    }

	    Book book = reservation.getBook();

	    reservation.setStatus(Status.RETURNED);
	    reservation.setReturnDate(LocalDate.now());

	    book.setAvailableCopies(book.getAvailableCopies() + 1);
	    book.setIsAvailable(true);

	    bookRepository.save(book);

	    return reservationRepository.save(reservation);
	}
}