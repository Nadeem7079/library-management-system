package com.library.librarysystem.controller;

import com.library.librarysystem.entity.Reservation; 
import com.library.librarysystem.service.ReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {
	

	private final ReservationService reservationService;
	
	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}
	
	
	@PostMapping("/reservations")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public Reservation reserveBook(@RequestParam Long userId, @RequestParam Long bookId){
	    return reservationService.reserveBook(userId, bookId);
	}

	@GetMapping("/users/{id}/reservations")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public List<Reservation> getUserReservations(
	        @PathVariable Long id,
	        Authentication authentication) {

	    return reservationService.getReservationByUser(
	            id,
	            authentication.getName(),
	            authentication.getAuthorities()
	    );
	}

	@GetMapping("/admin/reservations")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Reservation> getAdminReservations(){
	    return reservationService.getAllReservations();
	}

	@PutMapping("/reservations/{reservationId}/return")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public Reservation returnBook(
	        @PathVariable Long reservationId,
	        Authentication authentication) {

	    return reservationService.returnBook(
	            reservationId,
	            authentication
	    );
	}
	
}
