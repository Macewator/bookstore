package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.Publisher;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private static final String AUTHOR_SORT = "author";
    private static final String PRICE_SORT = "price";

    private static final String TITLE_SEARCH = "title";
    private static final String AUTHOR_SEARCH = "author";

    private static final String DEFAULT_BOOK_STATUS = "default";

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

    public Page<Book> getAllBooksFromPublisher(Long id, int page) {
        return bookRepository.findAllByPublisher_Id(id, PageRequest.of(page - 1, 3));
    }

    public Page<Book> filterPublisherBooksByStatus(Long id, String status, int page) {
        return bookRepository.findAllByPublisher_IdAndStatus(id, status, PageRequest.of(page - 1, 3));
    }

    public List<Publisher> searchPublisher(String name) {
        return publisherRepository.findAllByPublisherNameContainingIgnoreCase(name);
    }

    public Page<Book> searchBooksFromPublisher(Long id, String value, String category, String status, int page) {
        if (status.equals(DEFAULT_BOOK_STATUS)) {
            return searchOptions(id, value, category, page);
        }
        return searchOptionsWithStatus(id, value, category, status, page);
    }

    private Page<Book> searchOptions(Long id, String value, String category, int page) {
        switch (category) {
            case TITLE_SEARCH:
                return bookRepository.findAllByPublisher_IdAndTitleContainingIgnoreCase(
                        id, value, PageRequest.of(page - 1, 3));
            case AUTHOR_SEARCH:
                return bookRepository.findAllByPublisher_IdAndAuthor_LastNameContainingIgnoreCase(
                        id, value, PageRequest.of(page - 1, 3));
            default:
                return bookRepository.findAll(PageRequest.of(page - 1, 3));
        }
    }

    private Page<Book> searchOptionsWithStatus(Long id, String value, String category, String status, int page) {
        switch (category) {
            case TITLE_SEARCH:
                return bookRepository.findAllByPublisher_IdAndTitleContainingIgnoreCaseAndStatus(
                        id, value, status, PageRequest.of(page - 1, 3));

            case AUTHOR_SEARCH:
                return bookRepository.findAllByPublisher_IdAndAuthor_LastNameContainingIgnoreCaseAndStatus(
                        id, value, status, PageRequest.of(page - 1, 3));
            default:
                return bookRepository.findAllByPublisher_IdAndStatus(id, status, PageRequest.of(page - 1, 3));
        }
    }

    private Page<Book> searchBooksFromPublisherWithSort(Long id, String value, String category, String status, int page, String sortType) {
        if (status == null || status.equals(DEFAULT_BOOK_STATUS)) {
            return searchOptionsWithSort(id, value, category, sortType, page);
        }
        return searchOptionsWithStatusAndSort(id,value, category, status, sortType, page);
    }

    private Page<Book> searchOptionsWithSort(Long id, String value, String category, String sortType, int page) {
        switch (category) {
            case TITLE_SEARCH:
                return bookRepository.findAllByPublisher_IdAndTitleContainingIgnoreCase(
                        id, value, PageRequest.of(page - 1, 3, Sort.by(sortType)));
            case AUTHOR_SEARCH:
                return bookRepository.findAllByPublisher_IdAndAuthor_LastNameContainingIgnoreCase(
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
            case AUTHOR_SEARCH:
                return bookRepository.findAllByAuthor_LastNameContainingIgnoreCaseAndStatus(
                        value, status, PageRequest.of(page - 1, 3, Sort.by(sortType)));
            default:
                return bookRepository.findAll(PageRequest.of(page - 1, 3));
        }
    }

    public Page<Book> sortPublisherBooks(Long id, String type, String option, String status, int page) {
        if (status == null || status.equals(DEFAULT_BOOK_STATUS)) {
            return sortPublisherBooksOptions(id, type, option, page);
        }
        return sortAuthorBooksOptionsWithStatus(id, type, option, status, page);
    }

    private Page<Book> sortPublisherBooksOptions(Long id, String type, String option, int page) {
        switch (type) {
            case AUTHOR_SORT:
                if (option.equals("up")) {
                    return bookRepository.findAllByPublisher_Id(
                            id, PageRequest.of(page - 1, 3, Sort.by("publisher")));
                } else {
                    return bookRepository.findAllByPublisher_Id(
                            id, PageRequest.of(page - 1, 3, Sort.by("publisher").descending()));
                }
            case PRICE_SORT:
                if (option.equals("up")) {
                    return bookRepository.findAllByPublisher_Id(
                            id, PageRequest.of(page - 1, 3, Sort.by("price")));
                } else {
                    return bookRepository.findAllByPublisher_Id(
                            id, PageRequest.of(page - 1, 3, Sort.by("price").descending()));
                }
            default:
                if (option.equals("up")) {
                    return bookRepository.findAllByPublisher_Id(
                            id, PageRequest.of(page - 1, 3, Sort.by("title".toLowerCase())));
                } else {
                    return bookRepository.findAllByPublisher_Id(
                            id, PageRequest.of(page - 1, 3, Sort.by("title".toLowerCase()).descending()));
                }
        }
    }

    private Page<Book> sortAuthorBooksOptionsWithStatus(Long id, String type, String option, String status, int page) {
        switch (type) {
            case AUTHOR_SORT:
                if (option.equals("up")) {
                    return bookRepository.findAllByPublisher_IdAndStatus(
                            id, status, PageRequest.of(page - 1, 3, Sort.by("publisher")));
                } else {
                    return bookRepository.findAllByPublisher_IdAndStatus(
                            id, status, PageRequest.of(page - 1, 3, Sort.by("publisher").descending()));
                }
            case PRICE_SORT:
                if (option.equals("up")) {
                    return bookRepository.findAllByPublisher_IdAndStatus(
                            id, status, PageRequest.of(page - 1, 3, Sort.by("price")));
                } else {
                    return bookRepository.findAllByPublisher_IdAndStatus(
                            id, status, PageRequest.of(page - 1, 3, Sort.by("price").descending()));
                }
            default:
                if (option.equals("up")) {
                    return bookRepository.findAllByPublisher_IdAndStatus(
                            id, status, PageRequest.of(page - 1, 3, Sort.by("title".toLowerCase())));
                } else {
                    return bookRepository.findAllByPublisher_IdAndStatus(
                            id, status, PageRequest.of(page - 1, 3, Sort.by("title".toLowerCase()).descending()));
                }
        }
    }

    public List<Book> sortOptionsAfterSearch(Long id, String type, String option, String value, String category, String status, int page, String sort) {
        switch (type) {
            case AUTHOR_SORT:
                if (option.equals("up")) {
                    return searchBooksFromPublisherWithSort(id,value,category,status,page,sort).getContent();
                } else {
                    return searchBooksFromPublisherWithSort(id,value,category,status,page,sort).get()
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
                }
            case PRICE_SORT:
                if (option.equals("up")) {
                    return searchBooksFromPublisherWithSort(id,value,category,status,page,sort).getContent();
                } else {
                    return searchBooksFromPublisherWithSort(id,value,category,status,page,sort).get()
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
                }
            default:
                if (option.equals("up")) {
                    return searchBooksFromPublisherWithSort(id,value,category,status,page,sort).getContent();
                } else {
                    return searchBooksFromPublisherWithSort(id,value,category,status,page,sort).get()
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
                }
        }
    }
}
