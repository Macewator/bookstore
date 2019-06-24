package com.bookstore.repository;

import com.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM book ORDER BY RAND() LIMIT 1")
    Book findRandomBook();

    @Query(nativeQuery = true, value = "SELECT * FROM book WHERE status=:status LIMIT 2")
    List<Book> findAllByStatus(@Param("status") String status);

    List<Book> findAllByTitleContainingIgnoreCase(String title);

    List<Book> findAllByAuthor_LastNameContainingIgnoreCase(String lastName);

    List<Book> findAllByPublisher_PublisherNameContainingIgnoreCase(String publisherName);
}
