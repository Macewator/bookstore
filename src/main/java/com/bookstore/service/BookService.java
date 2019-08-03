package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.Client;
import com.bookstore.model.Comment;
import com.bookstore.model.Rating;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.ClientRepository;
import com.bookstore.repository.CommentRepository;
import com.bookstore.repository.RatingRepository;
import com.bookstore.util.ClientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BookService {

    private static final String AUTHOR_SORT = "author";
    private static final String PUBLISHER_SORT = "publisher";
    private static final String PRICE_SORT = "price";

    private static final String TITLE_SEARCH = "title";
    private static final String AUTHOR_SEARCH = "author";
    private static final String PUBLISHER_SEARCH = "publisher";

    private static final String DEFAULT_BOOK_STATUS = "default";

    private BookRepository bookRepository;
    private ClientRepository clientRepository;
    private RatingRepository ratingRepository;
    private CommentRepository commentRepository;

    @Autowired
    public BookService(BookRepository bookRepository, ClientRepository clientRepository, RatingRepository ratingRepository, CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
        this.ratingRepository = ratingRepository;
        this.commentRepository = commentRepository;
    }

    public Page<Book> getAllBooks(int page) {
        return bookRepository.findAll(PageRequest.of(page - 1, 3));
    }

    public Book getBook(Long isbn) {
        return bookRepository.findById(isbn)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("książka nie isnieje");
                });
    }

    public Page<Book> sortBooks(String type, String option, String status, int page) {
        if (status == null || status.equals(DEFAULT_BOOK_STATUS)) {
            return sortOptions(type, option, page);
        }
        return sortOptionsWithStatus(type, option, status, page);
    }

    private Page<Book> sortOptions(String type, String option, int page) {
        switch (type) {
            case AUTHOR_SORT:
                if (option.equals("up")) {
                    return bookRepository.findAll(
                            PageRequest.of(page - 1, 3, Sort.by("author")));
                } else {
                    return bookRepository.findAll(
                            PageRequest.of(page - 1, 3, Sort.by("author").descending()));
                }
            case PUBLISHER_SORT:
                if (option.equals("up")) {
                    return bookRepository.findAll(
                            PageRequest.of(page - 1, 3, Sort.by("publisher")));
                } else {
                    return bookRepository.findAll(
                            PageRequest.of(page - 1, 3, Sort.by("publisher").descending()));
                }
            case PRICE_SORT:
                if (option.equals("up")) {
                    return bookRepository.findAll(
                            PageRequest.of(page - 1, 3, Sort.by("price")));
                } else {
                    return bookRepository.findAll(
                            PageRequest.of(page - 1, 3, Sort.by("price").descending()));
                }
            default:
                if (option.equals("up")) {
                    return bookRepository.findAll(
                            PageRequest.of(page - 1, 3, Sort.by("title".toLowerCase())));
                } else {
                    return bookRepository.findAll(
                            PageRequest.of(page - 1, 3, Sort.by("title".toLowerCase()).descending()));
                }
        }
    }

    private Page<Book> sortOptionsWithStatus(String type, String option, String status, int page) {
        switch (type) {
            case AUTHOR_SORT:
                if (option.equals("up")) {
                    return bookRepository.findAllByStatus(
                            status, PageRequest.of(page - 1, 3, Sort.by("author")));
                } else {
                    return bookRepository.findAllByStatus(
                            status, PageRequest.of(page - 1, 3, Sort.by("author").descending()));
                }
            case PUBLISHER_SORT:
                if (option.equals("up")) {
                    return bookRepository.findAllByStatus(
                            status, PageRequest.of(page - 1, 3, Sort.by("publisher")));
                } else {
                    return bookRepository.findAllByStatus(
                            status, PageRequest.of(page - 1, 3, Sort.by("publisher").descending()));
                }
            case PRICE_SORT:
                if (option.equals("up")) {
                    return bookRepository.findAllByStatus(
                            status, PageRequest.of(page - 1, 3, Sort.by("price")));
                } else {
                    return bookRepository.findAllByStatus(
                            status, PageRequest.of(page - 1, 3, Sort.by("price").descending()));
                }
            default:
                if (option.equals("up")) {
                    return bookRepository.findAllByStatus(
                            status, PageRequest.of(page - 1, 3, Sort.by("title".toLowerCase())));
                } else {
                    return bookRepository.findAllByStatus(
                            status, PageRequest.of(page - 1, 3, Sort.by("title".toLowerCase()).descending()));
                }
        }
    }

    public List<Book> sortOptionsAfterSearch(String type, String option, String value, String category, String status, int page, String sort) {
        switch (type) {
            case AUTHOR_SORT:
                if (option.equals("up")) {
                    return searchBooksWithSort(value,category,status,page,sort).getContent();
                } else {
                    return searchBooksWithSort(value,category,status,page,sort).get()
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
                }
            case PUBLISHER_SORT:
                if (option.equals("up")) {
                    return searchBooksWithSort(value,category,status,page,sort).getContent();
                } else {
                    return searchBooksWithSort(value,category,status,page,sort).get()
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
                }
            case PRICE_SORT:
                if (option.equals("up")) {
                    return searchBooksWithSort(value,category,status,page,sort).getContent();
                } else {
                    return searchBooksWithSort(value,category,status,page,sort).get()
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
                }
            default:
                if (option.equals("up")) {
                    return searchBooksWithSort(value,category,status,page,sort).getContent();
                } else {
                    return searchBooksWithSort(value,category,status,page,sort).get()
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
                }
        }
    }

    private Page<Book> searchBooksWithSort(String value, String category, String status, int page, String sortType) {
        if (status == null || status.equals(DEFAULT_BOOK_STATUS)) {
            return searchOptionsWithSort(value, category, sortType, page);
        }
        return searchOptionsWithStatusAndSort(value, category, status, sortType, page);
    }

    private Page<Book> searchOptionsWithSort(String value, String category, String sortType, int page) {
        switch (category) {
            case TITLE_SEARCH:
                return bookRepository.findAllByTitleContainingIgnoreCase(
                        value, PageRequest.of(page - 1, 3, Sort.by(sortType)));
            case AUTHOR_SEARCH:
                return bookRepository.findAllByAuthor_LastNameContainingIgnoreCase(
                        value, PageRequest.of(page - 1, 3, Sort.by(sortType)));
            case PUBLISHER_SEARCH:
                return bookRepository.findAllByPublisher_PublisherNameContainingIgnoreCase(
                        value, PageRequest.of(page - 1, 3, Sort.by(sortType)));
            default:
                return bookRepository.findAll(PageRequest.of(page - 1, 3));
        }
    }

    private Page<Book> searchOptionsWithStatusAndSort(String value, String category, String status, String sortType, int page) {
        switch (category) {
            case TITLE_SEARCH:
                return bookRepository.findAllByTitleContainingIgnoreCaseAndStatus(
                        value, status, PageRequest.of(page - 1, 3, Sort.by(sortType)));
            case AUTHOR_SEARCH:
                return bookRepository.findAllByAuthor_LastNameContainingIgnoreCaseAndStatus(
                        value, status, PageRequest.of(page - 1, 3, Sort.by(sortType)));
            case PUBLISHER_SEARCH:
                return bookRepository.findAllByPublisher_PublisherNameContainingIgnoreCaseAndStatus(
                        value, status, PageRequest.of(page - 1, 3, Sort.by(sortType)));
            default:
                return bookRepository.findAll(PageRequest.of(page - 1, 3));
        }
    }

    public Page<Book> searchBooks(String value, String category, String status, int page) {
        if (status == null || status.equals(DEFAULT_BOOK_STATUS)) {
            return searchOptions(value, category, page);
        }
        return searchOptionsWithStatus(value, category, status, page);
    }

    private Page<Book> searchOptions(String value, String category, int page) {
        switch (category) {
            case TITLE_SEARCH:
                return bookRepository.findAllByTitleContainingIgnoreCase(
                        value, PageRequest.of(page - 1, 3));
            case AUTHOR_SEARCH:
                return bookRepository.findAllByAuthor_LastNameContainingIgnoreCase(
                        value, PageRequest.of(page - 1, 3));
            case PUBLISHER_SEARCH:
                return bookRepository.findAllByPublisher_PublisherNameContainingIgnoreCase(
                        value, PageRequest.of(page - 1, 3));
            default:
                return bookRepository.findAll(PageRequest.of(page - 1, 3));
        }
    }

    private Page<Book> searchOptionsWithStatus(String value, String category, String status, int page) {
        switch (category) {
            case TITLE_SEARCH:
                return bookRepository.findAllByTitleContainingIgnoreCaseAndStatus(
                        value, status, PageRequest.of(page - 1, 3));
            case AUTHOR_SEARCH:
                return bookRepository.findAllByAuthor_LastNameContainingIgnoreCaseAndStatus(
                        value, status, PageRequest.of(page - 1, 3));
            case PUBLISHER_SEARCH:
                return bookRepository.findAllByPublisher_PublisherNameContainingIgnoreCaseAndStatus(
                        value, status, PageRequest.of(page - 1, 3));
            default:
                return bookRepository.findAllByStatus(status,PageRequest.of(page - 1, 3));
        }
    }

    public Page<Book> filterBooksByStatus(String status, int page) {
        return bookRepository.findAllByStatus(status, PageRequest.of(page - 1, 3));
    }

    public Book bookRating(Long isbn, String bookRating, String userName) {
        Book book = getBook(isbn);
        Client client = clientRepository.findByUserName(userName)
                .orElseThrow(() -> new ClientNotFoundException("użytkownik nie istnieje"));
        Rating rating = new Rating();
        rating.setBookRating(Double.valueOf(bookRating));
        rating.setClient(client);
        rating.setBook(book);
        ratingRepository.save(rating);
        return book;
    }

    public boolean ratingCheck(String userName, Long isbn) {
        List<Long> isbnList = ratingRepository.findAllIsbnByUserName(userName);
        return isbnList.contains(isbn);
    }

    public Double calculateBookRating(Long isbn) {
        List<Double> ratings = ratingRepository.findAllRatingByIsbn(isbn);
        return ratings.stream().reduce(0.0, Double::sum) / ratings.size();
    }

    public void deleteClientRating(String userName) {
        ratingRepository.deleteAllByClient_UserName(userName);
    }

    public Book addComment(String clientComment, String userName, Long isbn) {
        Book book = getBook(isbn);
        Client client = clientRepository.findByUserName(userName)
                .orElseThrow(() -> new ClientNotFoundException("użytkownik nie istnieje"));
        Comment comment = new Comment();
        comment.setClient(client);
        comment.setBook(book);
        comment.setClientComment(clientComment);
        commentRepository.save(comment);
        return book;
    }

    public List<Comment> getAllBookComments(Long isbn) {
        return commentRepository.findAllByBook_Isbn(isbn);
    }

    public void deleteClientComments(String userName) {
        commentRepository.deleteAllByClient_UserName(userName);
    }
}
