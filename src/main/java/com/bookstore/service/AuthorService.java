package com.bookstore.service;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private static final String PUBLISHER_SORT = "publisher";
    private static final String PRICE_SORT = "price";

    private static final String TITLE_SEARCH = "title";
    private static final String PUBLISHER_SEARCH = "publisher";

    private static final String EMPTY_STATUS = "empty";

    private AuthorRepository authorRepository;
    private BookRepository bookRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Book> getAllBooksFromAuthor(Long id) {
        return bookRepository.findAllByAuthor_Id(id);
    }

    public List<Author> searchAuthor(String name) {
        return authorRepository.findAllByLastNameContainingIgnoreCase(name);
    }

    public List<Book> searchBooksFromAuthor(Long authorId, String value, String category, String status) {
        if (status.equals(EMPTY_STATUS)) {
            return searchOptions(authorId, value, category);
        }
        return searchOptionsWithStatus(authorId, value, category, status);
    }

    private List<Book> searchOptions(Long authorId, String value, String category) {
        switch (category) {
            case TITLE_SEARCH:
                return getAllBooksFromAuthor(authorId).stream()
                        .filter(book -> dataValuesToLowerCase(book.getTitle(), value))
                        .collect(Collectors.toList());
            case PUBLISHER_SEARCH:
                return getAllBooksFromAuthor(authorId).stream()
                        .filter(book -> dataValuesToLowerCase(book.getPublisher().getPublisherName(), value))
                        .collect(Collectors.toList());
            default:
                return getAllBooksFromAuthor(authorId);
        }
    }

    private List<Book> searchOptionsWithStatus(Long authorId, String value, String category, String status) {
        switch (category) {
            case TITLE_SEARCH:
                return getAllBooksFromAuthor(authorId).stream()
                        .filter(book -> book.getStatus().equals(status))
                        .filter(book -> dataValuesToLowerCase(book.getTitle(), value))
                        .collect(Collectors.toList());

            case PUBLISHER_SEARCH:
                return getAllBooksFromAuthor(authorId).stream()
                        .filter(book -> book.getStatus().equals(status))
                        .filter(book -> dataValuesToLowerCase(book.getPublisher().getPublisherName(), value))
                        .collect(Collectors.toList());
            default:
                return getAllBooksFromAuthor(authorId);
        }
    }

    private boolean dataValuesToLowerCase(String firstValue, String secondValue) {
        return firstValue.toLowerCase().contains(secondValue.toLowerCase());
    }

    public List<Book> sortAuthorBooks(String type, String option, List<Book> books) {
        switch (type) {
            case PUBLISHER_SORT:
                if (option.equals("up")) {
                    return books.stream()
                            .sorted(Comparator.comparing(Book::getPublisher))
                            .collect(Collectors.toList());
                } else {
                    return books.stream()
                            .sorted(Comparator.comparing(Book::getPublisher).reversed())
                            .collect(Collectors.toList());
                }
            case PRICE_SORT:
                if (option.equals("up")) {
                    return books.stream()
                            .sorted(Comparator.comparing(Book::getPrice))
                            .collect(Collectors.toList());
                } else {
                    return books.stream()
                            .sorted(Comparator.comparing(Book::getPrice).reversed())
                            .collect(Collectors.toList());
                }
            default:
                if (option.equals("up")) {
                    return books.stream()
                            .sorted(Comparator.naturalOrder())
                            .collect(Collectors.toList());
                } else {
                    return books.stream()
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
                }
        }
    }

    public List<Book> filterAuthorBooksByStatus(String status, Long id) {
        return getAllBooksFromAuthor(id).stream()
                .filter(book -> book.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
