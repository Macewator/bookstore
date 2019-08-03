package com.bookstore.repository;

import com.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    List<Book> findAllByStatusForHomePage(@Param("status") String status);

    Page<Book> findAllByStatus(String status, Pageable pageable);

    Page<Book> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Book> findAllByTitleContainingIgnoreCaseAndStatus(String title, String status, Pageable pageable);

    Page<Book> findAllByAuthor_LastNameContainingIgnoreCase(String lastName, Pageable pageable);

    Page<Book> findAllByAuthor_LastNameContainingIgnoreCaseAndStatus(String lastName, String status, Pageable pageable);

    Page<Book> findAllByPublisher_PublisherNameContainingIgnoreCase(String publisherName, Pageable pageable);

    Page<Book> findAllByPublisher_PublisherNameContainingIgnoreCaseAndStatus(String publisherName, String status, Pageable pageable);

    Page<Book> findAllByPublisher_Id(Long Id, Pageable pageable);

    Page<Book> findAllByPublisher_IdAndStatus(Long id, String status, Pageable pageable);

    Page<Book> findAllByPublisher_IdAndTitleContainingIgnoreCase(Long id, String title, Pageable pageable);

    Page<Book> findAllByPublisher_IdAndTitleContainingIgnoreCaseAndStatus(Long id, String title, String status, Pageable pageable);

    Page<Book> findAllByPublisher_IdAndAuthor_LastNameContainingIgnoreCase(Long id, String publisherName, Pageable pageable);

    Page<Book> findAllByPublisher_IdAndAuthor_LastNameContainingIgnoreCaseAndStatus(Long id, String publisherName, String status, Pageable pageable);

    Page<Book> findAllByAuthor_Id(Long id, Pageable pageable);

    Page<Book> findAllByAuthor_IdAndStatus(Long id, String status, Pageable pageable);

    Page<Book> findAllByAuthor_IdAndTitleContainingIgnoreCase(Long id, String title, Pageable pageable);

    Page<Book> findAllByAuthor_IdAndTitleContainingIgnoreCaseAndStatus(Long id, String title, String status, Pageable pageable);

    Page<Book> findAllByAuthor_IdAndPublisher_PublisherNameContainingIgnoreCase(Long id, String publisherName, Pageable pageable);

    Page<Book> findAllByAuthor_IdAndPublisher_PublisherNameContainingIgnoreCaseAndStatus(Long id, String publisherName, String status, Pageable pageable);
}
