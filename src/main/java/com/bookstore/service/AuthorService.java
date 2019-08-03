package com.bookstore.service;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private static final String DEFAULT_BOOK_STATUS = "default";

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

    public Page<Book> getAllBooksFromAuthor(Long id, int page) {
        return bookRepository.findAllByAuthor_Id(id, PageRequest.of(page - 1, 3));
    }

    public Page<Book> filterAuthorBooksByStatus(Long id, String status, int page) {
        return bookRepository.findAllByAuthor_IdAndStatus(id, status, PageRequest.of(page - 1, 3));
    }

    public List<Author> searchAuthor(String name) {
        return authorRepository.findAllByLastNameContainingIgnoreCase(name);
    }

    public Page<Book> searchBooksFromAuthor(Long id, String value, String category, String status, int page) {
        if (status.equals(DEFAULT_BOOK_STATUS)) {
            return searchOptions(id, value, category, page);
        }
        return searchOptionsWithStatus(id, value, category, status, page);
    }

    private Page<Book> searchOptions(Long id, String value, String category, int page) {
        switch (category) {
            case TITLE_SEARCH:
                return bookRepository.findAllByAuthor_IdAndTitleContainingIgnoreCase(
                        id, value, PageRequest.of(page - 1, 3));
            case PUBLISHER_SEARCH:
                return bookRepository.findAllByAuthor_IdAndPublisher_PublisherNameContainingIgnoreCase(
                        id, value, PageRequest.of(page - 1, 3));
            default:
                return bookRepository.findAll(PageRequest.of(page - 1, 3));
        }
    }

    private Page<Book> searchOptionsWithStatus(Long id, String value, String category, String status, int page) {
        switch (category) {
            case TITLE_SEARCH:
                return bookRepository.findAllByAuthor_IdAndTitleContainingIgnoreCaseAndStatus(
                        id, value, status, PageRequest.of(page - 1, 3));

            case PUBLISHER_SEARCH:
                return bookRepository.findAllByAuthor_IdAndPublisher_PublisherNameContainingIgnoreCaseAndStatus(
                        id, value, status, PageRequest.of(page - 1, 3));
            default:
                return bookRepository.findAllByAuthor_IdAndStatus(id, status, PageRequest.of(page - 1, 3));
        }
    }

    private Page<Book> searchBooksFromAuthorWithSort(Long id, String value, String category, String status, int page, String sortType) {
        if (status == null || status.equals(DEFAULT_BOOK_STATUS)) {
            return searchOptionsWithSort(id, value, category, sortType, page);
        }
        return searchOptionsWithStatusAndSort(id,value, category, status, sortType, page);
    }

    private Page<Book> searchOptionsWithSort(Long id, String value, String category, String sortType, int page) {
        switch (category) {
            case TITLE_SEARCH:
                return bookRepository.findAllByAuthor_IdAndTitleContainingIgnoreCase(
                        id, value, PageRequest.of(page - 1, 3, Sort.by(sortType)));
            case PUBLISHER_SEARCH:
                return bookRepository.findAllByAuthor_IdAndPublisher_PublisherNameContainingIgnoreCase(
                        id, value, PageRequest.of(page - 1, 3, Sort.by(sortType)));
            default:
                return bookRepository.findAll(PageRequest.of(page - 1, 3));
        }
    }

    private Page<Book> searchOptionsWithStatusAndSort(Long id, String value, String category, String status, String sortType, int page) {
        switch (category) {
            case TITLE_SEARCH:
                return bookRepository.findAllByTitleContainingIgnoreCaseAndStatus(
                        value, status, PageRequest.of(page - 1, 3, Sort.by(sortType)));
            case PUBLISHER_SEARCH:
                return bookRepository.findAllByPublisher_PublisherNameContainingIgnoreCaseAndStatus(
                        value, status, PageRequest.of(page - 1, 3, Sort.by(sortType)));
            default:
                return bookRepository.findAll(PageRequest.of(page - 1, 3));
        }
    }

    public Page<Book> sortAuthorBooks(Long id, String type, String option, String status, int page) {
        if (status == null || status.equals(DEFAULT_BOOK_STATUS)) {
            return sortAuthorBooksOptions(id, type, option, page);
        }
        return sortAuthorBooksOptionsWithStatus(id, type, option, status, page);
    }

    private Page<Book> sortAuthorBooksOptions(Long id, String type, String option, int page) {
        switch (type) {
            case PUBLISHER_SORT:
                if (option.equals("up")) {
                    return bookRepository.findAllByAuthor_Id(
                            id, PageRequest.of(page - 1, 3, Sort.by("publisher")));
                } else {
                    return bookRepository.findAllByAuthor_Id(
                            id, PageRequest.of(page - 1, 3, Sort.by("publisher").descending()));
                }
            case PRICE_SORT:
                if (option.equals("up")) {
                    return bookRepository.findAllByAuthor_Id(
                            id, PageRequest.of(page - 1, 3, Sort.by("price")));
                } else {
                    return bookRepository.findAllByAuthor_Id(
                            id, PageRequest.of(page - 1, 3, Sort.by("price").descending()));
                }
            default:
                if (option.equals("up")) {
                    return bookRepository.findAllByAuthor_Id(
                            id, PageRequest.of(page - 1, 3, Sort.by("title".toLowerCase())));
                } else {
                    return bookRepository.findAllByAuthor_Id(
                            id, PageRequest.of(page - 1, 3, Sort.by("title".toLowerCase()).descending()));
                }
        }
    }

    private Page<Book> sortAuthorBooksOptionsWithStatus(Long id, String type, String option, String status, int page) {
        switch (type) {
            case PUBLISHER_SORT:
                if (option.equals("up")) {
                    return bookRepository.findAllByAuthor_IdAndStatus(
                            id, status, PageRequest.of(page - 1, 3, Sort.by("publisher")));
                } else {
                    return bookRepository.findAllByAuthor_IdAndStatus(
                            id, status, PageRequest.of(page - 1, 3, Sort.by("publisher").descending()));
                }
            case PRICE_SORT:
                if (option.equals("up")) {
                    return bookRepository.findAllByAuthor_IdAndStatus(
                            id, status, PageRequest.of(page - 1, 3, Sort.by("price")));
                } else {
                    return bookRepository.findAllByAuthor_IdAndStatus(
                            id, status, PageRequest.of(page - 1, 3, Sort.by("price").descending()));
                }
            default:
                if (option.equals("up")) {
                    return bookRepository.findAllByAuthor_IdAndStatus(
                            id, status, PageRequest.of(page - 1, 3, Sort.by("title".toLowerCase())));
                } else {
                    return bookRepository.findAllByAuthor_IdAndStatus(
                            id, status, PageRequest.of(page - 1, 3, Sort.by("title".toLowerCase()).descending()));
                }
        }
    }

    public List<Book> sortOptionsAfterSearch(Long id, String type, String option, String value, String category, String status, int page, String sort) {
        switch (type) {
            case PUBLISHER_SORT:
                if (option.equals("up")) {
                    return searchBooksFromAuthorWithSort(id,value,category,status,page,sort).getContent();
                } else {
                    return searchBooksFromAuthorWithSort(id,value,category,status,page,sort).get()
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
                }
            case PRICE_SORT:
                if (option.equals("up")) {
                    return searchBooksFromAuthorWithSort(id,value,category,status,page,sort).getContent();
                } else {
                    return searchBooksFromAuthorWithSort(id,value,category,status,page,sort).get()
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
                }
            default:
                if (option.equals("up")) {
                    return searchBooksFromAuthorWithSort(id,value,category,status,page,sort).getContent();
                } else {
                    return searchBooksFromAuthorWithSort(id,value,category,status,page,sort).get()
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
                }
        }
    }
}