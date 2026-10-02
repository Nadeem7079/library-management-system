package com.library.librarysystem.repository;

import com.library.librarysystem.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import com.library.librarysystem.entity.*;

import java.util.*;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
	
	List<Reservation> findByUserId(Long userId);
	
	boolean existsByBookIdAndStatus(Long bookId, Status status);
	boolean existsByUserIdAndStatus(Long userId, Status status);
}
