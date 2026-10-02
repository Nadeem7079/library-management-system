package com.library.librarysystem.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "ISBN is required")
    @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters")
    private String isbn;

    private Boolean deleted = false;

    @NotNull(message = "Available status is required")
    @Column(nullable = false)
    private Boolean available = true;

    @NotNull(message = "Available copies is required")
    @Min(value = 0, message = "Available copies cannot be negative")
    @Column(name = "available_copies", nullable = false)
    private Integer availableCopies = 1;
    
    
    
    
    // Default constructor (required)
    public Book() {}

    // Parameterized constructor
    public Book(String title, String author, String isbn, Boolean available, Integer availableCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = available != null ? available : true;
        this.availableCopies = availableCopies != null ? availableCopies : 1;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public Boolean isAvailable() {
        return available;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }
    
    public Boolean getDeleted() {
    	return deleted;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setIsAvailable(Boolean available) {
        this.available = available;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }
    
    public void setDeleted(Boolean deleted) {
    	this.deleted = deleted;
    }
}