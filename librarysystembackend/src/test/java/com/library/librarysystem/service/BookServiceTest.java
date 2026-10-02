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

import com.library.librarysystem.entity.Book;
import com.library.librarysystem.entity.Status;
import com.library.librarysystem.exception.ActiveReservationException;
import com.library.librarysystem.exception.DuplicateResourceException;
import com.library.librarysystem.exception.ResourceNotFoundException;
import com.library.librarysystem.repository.BookRepository;
import com.library.librarysystem.repository.ReservationRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {

        book = new Book();

        book.setId(1L);
        book.setTitle("Java Basics");
        book.setAuthor("James");
        book.setIsbn("1234567890");
        book.setAvailableCopies(5);
        book.setIsAvailable(true);
        book.setDeleted(false);
    }

    // =========================================================
    // GET ALL BOOKS
    // =========================================================

    @Test
    void getAllBooks_success() {

        when(bookRepository.findByDeletedFalse())
                .thenReturn(List.of(book));

        List<Book> result =
                bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java Basics", result.get(0).getTitle());

        verify(bookRepository)
                .findByDeletedFalse();
    }

    // =========================================================
    // GET BOOK BY ID
    // =========================================================

    @Test
    void getBookById_success() {

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        Book result =
                bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Java Basics", result.getTitle());
        assertEquals("James", result.getAuthor());
        assertEquals("1234567890", result.getIsbn());

        verify(bookRepository)
                .findById(1L);
    }

    @Test
    void getBookById_notFound_throwsException() {

        when(bookRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.getBookById(999L)
        );

        verify(bookRepository)
                .findById(999L);
    }

    // =========================================================
    // ADD BOOK
    // =========================================================

    @Test
    void addBook_success() {

        when(bookRepository.existsByIsbn(book.getIsbn()))
                .thenReturn(false);

        when(bookRepository.save(book))
                .thenReturn(book);

        Book result =
                bookService.addBook(book);

        assertNotNull(result);
        assertEquals("Java Basics", result.getTitle());
        assertEquals("1234567890", result.getIsbn());

        verify(bookRepository)
                .existsByIsbn("1234567890");

        verify(bookRepository)
                .save(book);
    }

    @Test
    void addBook_duplicateIsbn_throwsException() {

        when(bookRepository.existsByIsbn(book.getIsbn()))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> bookService.addBook(book)
        );

        verify(bookRepository, never())
                .save(any(Book.class));
    }

    // =========================================================
    // UPDATE BOOK
    // =========================================================

    @Test
    void updateBook_success() {

        Book updatedBook = new Book();

        updatedBook.setTitle("Advanced Java");
        updatedBook.setAuthor("James Gosling");
        updatedBook.setIsbn("1234567890");
        updatedBook.setAvailableCopies(10);
        updatedBook.setIsAvailable(true);

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(bookRepository.save(book))
                .thenReturn(book);

        Book result =
                bookService.updateBook(1L, updatedBook);

        assertNotNull(result);

        assertEquals(
                "Advanced Java",
                result.getTitle()
        );

        assertEquals(
                "James Gosling",
                result.getAuthor()
        );

        assertEquals(
                "1234567890",
                result.getIsbn()
        );

        assertEquals(
                10,
                result.getAvailableCopies()
        );

        assertTrue(result.isAvailable());

        verify(bookRepository)
                .findById(1L);

        verify(bookRepository)
                .save(book);
    }

    @Test
    void updateBook_notFound_throwsException() {

        Book updatedBook = new Book();

        updatedBook.setTitle("Advanced Java");
        updatedBook.setAuthor("James");
        updatedBook.setIsbn("1234567890");
        updatedBook.setAvailableCopies(10);
        updatedBook.setIsAvailable(true);

        when(bookRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.updateBook(
                        999L,
                        updatedBook
                )
        );

        verify(bookRepository, never())
                .save(any(Book.class));
    }

    @Test
    void updateBook_duplicateIsbn_throwsException() {

        Book updatedBook = new Book();

        updatedBook.setTitle("Advanced Java");
        updatedBook.setAuthor("James");
        updatedBook.setIsbn("9999999999");
        updatedBook.setAvailableCopies(10);
        updatedBook.setIsAvailable(true);

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(bookRepository.existsByIsbn("9999999999"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> bookService.updateBook(
                        1L,
                        updatedBook
                )
        );

        verify(bookRepository, never())
                .save(any(Book.class));
    }

    // =========================================================
    // DELETE BOOK
    // =========================================================

    @Test
    void deleteBook_success() {

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(
                reservationRepository
                        .existsByBookIdAndStatus(
                                1L,
                                Status.ACTIVE
                        )
        ).thenReturn(false);

        bookService.deleteBook(1L);

        assertTrue(book.getDeleted());

        verify(bookRepository)
                .save(book);
    }

    @Test
    void deleteBook_notFound_throwsException() {

        when(bookRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.deleteBook(999L)
        );

        verify(bookRepository, never())
                .save(any(Book.class));

        verify(
                reservationRepository,
                never()
        ).existsByBookIdAndStatus(
                anyLong(),
                any(Status.class)
        );
    }

    @Test
    void deleteBook_activeReservation_throwsException() {

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(
                reservationRepository
                        .existsByBookIdAndStatus(
                                1L,
                                Status.ACTIVE
                        )
        ).thenReturn(true);

        assertThrows(
                ActiveReservationException.class,
                () -> bookService.deleteBook(1L)
        );

        assertFalse(book.getDeleted());

        verify(bookRepository, never())
                .save(any(Book.class));
    }
}