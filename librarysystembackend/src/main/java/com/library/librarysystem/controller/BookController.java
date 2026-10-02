package com.library.librarysystem.controller;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.library.librarysystem.entity.Book;
import com.library.librarysystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name ="Book APIs", description ="Operations Related to books")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Add Book - only ADMIN can add
    @Operation(summary = "Add a new book", description = "Only admin can add books")
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public Book addBook(@Valid @RequestBody Book book) {
        book.setIsAvailable(true); // default available
        return bookService.addBook(book);
    }

    // Get All Books - ADMIN and USER
    @Operation(summary = "Get all books", description ="Fetch all available boooks in the laibrary")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // Get Book By Id - ADMIN and USER
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    // Update Book - only ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Book updateBook(
            @PathVariable Long id,
            @Valid @RequestBody Book book) {

        return bookService.updateBook(id, book);
    }

    // Delete Book - only ADMIN
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}