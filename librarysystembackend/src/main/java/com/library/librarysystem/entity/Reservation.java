package com.library.librarysystem.entity;

import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name="reservations")
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	private LocalDate issueDate;
	
	private LocalDate returnDate;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="book_id")
	private Book book;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	
	public Reservation() {}
	
	public Long getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}
	
	public Book getBook() {
		return book;
	}
	
	public LocalDate getIssueDate() {
		return issueDate;
	}
	
	public LocalDate getReturnDate() {
		return returnDate;
	}
	
	public Status getStatus(){
		return status;
	}
	
	
	
	
	public void setId(Long id) {
		this.id= id;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setBook(Book book){
		this.book = book;
	}
	
	public void setIssueDate(LocalDate issueDate){
		this.issueDate = issueDate;
	}
	
	public void setReturnDate(LocalDate returnDate){
		this.returnDate = returnDate;
	}
	
	public void setStatus(Status status){
		this.status = status;
	}

}
