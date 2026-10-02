package com.library.librarysystem.service;

import com.library.librarysystem.entity.Book;

import com.library.librarysystem.entity.Status;
import com.library.librarysystem.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import com.library.librarysystem.repository.*;
import com.library.librarysystem.exception.ResourceNotFoundException;
import com.library.librarysystem.exception.DuplicateResourceException;
import com.library.librarysystem.exception.ActiveReservationException;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	public List<Book> getAllBooks(){
		return bookRepository.findByDeletedFalse();
	}
	
	public Book getBookById(Long id) {
	    return bookRepository.findById(id)
	            .orElseThrow(() ->
	                new ResourceNotFoundException("Book not found with id: " + id)
	            );
	}
	
	public Book addBook(Book book) {

	    if (bookRepository.existsByIsbn(book.getIsbn())) {
	        throw new DuplicateResourceException(
	                "Book with ISBN " + book.getIsbn() + " already exists"
	        );
	    }

	    return bookRepository.save(book);
	}
	
	
	
	public Book updateBook(Long id, Book updatedBook) {

	    Book book = bookRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Book not found with id: " + id
	                    )
	            );

	    if (!book.getIsbn().equals(updatedBook.getIsbn())
	            && bookRepository.existsByIsbn(updatedBook.getIsbn())) {

	        throw new DuplicateResourceException(
	                "Book with ISBN " + updatedBook.getIsbn() + " already exists"
	        );
	    }

	    book.setTitle(updatedBook.getTitle());
	    book.setAuthor(updatedBook.getAuthor());
	    book.setIsbn(updatedBook.getIsbn());
	    book.setAvailableCopies(updatedBook.getAvailableCopies());
	    book.setIsAvailable(updatedBook.isAvailable());
	    book.setDeleted(updatedBook.getDeleted());

	    return bookRepository.save(book);
	}	
	
	
	public void deleteBook(Long id) {

	    Book book = bookRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Book not found with id: " + id
	                    )
	            );

	    boolean hasActiveReservations =
	            reservationRepository.existsByBookIdAndStatus(
	                    id, Status.ACTIVE
	            );

	    if (hasActiveReservations) {
	        throw new ActiveReservationException(
	                "Cannot delete Book with active reservations"
	        );
	    }

	    book.setDeleted(true);
	    bookRepository.save(book);
	}
}