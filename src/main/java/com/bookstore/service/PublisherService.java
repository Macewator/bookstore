package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.Publisher;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@SessionAttributes({"url", "entity_id", "option"})
public class PublisherService {

    private static final String AUTHOR_SORT = "author";
    private static final String PRICE_SORT = "price";

    private static final String TITLE_SEARCH = "title";
    private static final String AUTHOR_SEARCH = "author";

    private static final String EMPTY_STATUS = "empty";

    private PublisherRepository publisherRepository;
    private BookRepository bookRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository, BookRepository bookRepository) {
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
    }

    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    public List<Book> getAllBooksFromPublisher(Long id) {
        return bookRepository.findAllByPublisher_Id(id);
    }

    public List<Publisher> searchPublisher(String name) {
        return publisherRepository.findAllByPublisherNameContainingIgnoreCase(name);
    }

    public List<Book> searchBooksFromPublisher(Long publisherId, String value, String category, String status) {
        if (status.equals(EMPTY_STATUS)) {
            return searchOptions(publisherId, value, category);
        }
        return searchOptionsWithStatus(publisherId, value, category, status);
    }

    private List<Book> searchOptions(Long publisherId, String value, String category) {
        switch (category) {
            case TITLE_SEARCH:
                return getAllBooksFromPublisher(publisherId).stream()
                        .filter(book -> dataValuesToLowerCase(book.getTitle(),value))
                        .collect(Collectors.toList());
            case AUTHOR_SEARCH:
                return getAllBooksFromPublisher(publisherId).stream()
                        .filter(book -> dataValuesToLowerCase(book.getAuthor().getLastName(),value))
                        .collect(Collectors.toList());
            default:
                return getAllBooksFromPublisher(publisherId);
        }
    }

    private List<Book> searchOptionsWithStatus(Long publisherId, String value, String category, String status) {
        switch (category) {
            case TITLE_SEARCH:
                return getAllBooksFromPublisher(publisherId).stream()
                        .filter(book -> book.getStatus().equals(status))
                        .filter(book -> dataValuesToLowerCase(book.getTitle(),value))
                        .collect(Collectors.toList());

            case AUTHOR_SEARCH:
                return getAllBooksFromPublisher(publisherId).stream()
                        .filter(book -> book.getStatus().equals(status))
                        .filter(book -> dataValuesToLowerCase(book.getAuthor().getLastName(),value))
                        .collect(Collectors.toList());
            default:
                return getAllBooksFromPublisher(publisherId);
        }
    }

    private boolean dataValuesToLowerCase(String firstValue, String secondValue){
        return firstValue.toLowerCase().contains(secondValue.toLowerCase());
    }

    public List<Book> sortPublisherBooks(String type, String option, List<Book> books) {
        switch (type) {
            case AUTHOR_SORT:
                if (option.equals("up")) {
                    return books.stream()
                            .sorted(Comparator.comparing(Book::getAuthor))
                            .collect(Collectors.toList());
                } else {
                    return books.stream()
                            .sorted(Comparator.comparing(Book::getAuthor).reversed())
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

    public List<Book> filterPublisherBooksByStatus(String status, Long id) {
        return getAllBooksFromPublisher(id).stream()
                .filter(book -> book.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
