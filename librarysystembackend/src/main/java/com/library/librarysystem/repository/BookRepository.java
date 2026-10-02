package com.library.librarysystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.library.librarysystem.entity.Book;
import java.util.*;

public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByDeletedFalse();
	boolean existsByIsbn(String isbn);
}
