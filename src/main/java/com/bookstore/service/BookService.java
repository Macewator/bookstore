package com.bookstore.service;

import com.bookstore.dto.BookDto;
import com.bookstore.dto.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private static final String AUTHOR_SORT = "author";
    private static final String PUBLISHER_SORT = "publisher";
    private static final String PRICE_SORT = "price";

    private static final String TITLE_SEARCH = "title";
    private static final String AUTHOR_SEARCH = "author";
    private static final String PUBLISHER_SEARCH = "publisher";


    private BookRepository repository;

    @Autowired
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<BookDto> getAllBooks() {
        return repository.findAll().stream()
                .map(BookMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public BookDto getBookDetails(Long isbn) {
        return repository.findById(isbn)
                .map(BookMapper::entityToDto)
                .get();
    }

    public List<BookDto> sortBooks(String type) {
        switch (type) {
            case AUTHOR_SORT:
                return repository.findAll().stream()
                        .sorted(Comparator.comparing(Book::getAuthor))
                        .map(BookMapper::entityToDto)
                        .collect(Collectors.toList());
            case PUBLISHER_SORT:
                return repository.findAll().stream()
                        .sorted(Comparator.comparing(Book::getPublisher))
                        .map(BookMapper::entityToDto)
                        .collect(Collectors.toList());
            case PRICE_SORT:
                return repository.findAll().stream()
                        .sorted(Comparator.comparing(Book::getPrice))
                        .map(BookMapper::entityToDto)
                        .collect(Collectors.toList());
            default:
                return repository.findAll().stream()
                        .map(BookMapper::entityToDto)
                        .collect(Collectors.toList());
        }
    }

    public List<BookDto> searchBooks(String value, String category) {
        switch (category) {
            case TITLE_SEARCH:
                return repository.findAllByTitleContainingIgnoreCase(value).stream()
                        .map(BookMapper::entityToDto)
                        .collect(Collectors.toList());
            case AUTHOR_SEARCH:
                return repository.findAllByAuthor_LastNameContainingIgnoreCase(value).stream()
                        .map(BookMapper::entityToDto)
                        .collect(Collectors.toList());
            case PUBLISHER_SEARCH:
                return repository.findAllByPublisher_PublisherNameContainingIgnoreCase(value).stream()
                        .map(BookMapper::entityToDto)
                        .collect(Collectors.toList());
            default:
                return repository.findAll().stream()
                        .map(BookMapper::entityToDto)
                        .collect(Collectors.toList());
        }
    }
}
