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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class BookService {

    private static final String AUTHOR_SORT = "author";
    private static final String PUBLISHER_SORT = "publisher";
    private static final String PRICE_SORT = "price";

    private static final String TITLE_SEARCH = "title";
    private static final String AUTHOR_SEARCH = "author";
    private static final String PUBLISHER_SEARCH = "publisher";

    private static final String EMPTY_STATUS = "empty";

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

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBook(Long isbn) {
        return bookRepository.findById(isbn)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("książka nie isnieje");
                });
    }

    public List<Book> sortBooks(String type, String option, List<Book> books) {
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

    public List<Book> searchBooks(String value, String category, String status) {
        if (status.equals(EMPTY_STATUS)) {
            return searchOptions(value, category);
        }
        return searchOptionsWithStatus(value, category, status);
    }

    private List<Book> searchOptions(String value, String category) {
        switch (category) {
            case TITLE_SEARCH:
                return bookRepository.findAllByTitleContainingIgnoreCase(value);
            case AUTHOR_SEARCH:
                return bookRepository.findAllByAuthor_LastNameContainingIgnoreCase(value);
            case PUBLISHER_SEARCH:
                return bookRepository.findAllByPublisher_PublisherNameContainingIgnoreCase(value);
            default:
                return bookRepository.findAll();
        }
    }

    private List<Book> searchOptionsWithStatus(String value, String category, String status) {
        switch (category) {
            case TITLE_SEARCH:
                return bookRepository.findAllByTitleContainingIgnoreCase(value).stream()
                        .filter(book -> book.getStatus().equals(status))
                        .collect(Collectors.toList());
            case AUTHOR_SEARCH:
                return bookRepository.findAllByAuthor_LastNameContainingIgnoreCase(value).stream()
                        .filter(book -> book.getStatus().equals(status))
                        .collect(Collectors.toList());
            case PUBLISHER_SEARCH:
                return bookRepository.findAllByPublisher_PublisherNameContainingIgnoreCase(value).stream()
                        .filter(book -> book.getStatus().equals(status))
                        .collect(Collectors.toList());
            default:
                return bookRepository.findAll();
        }
    }

    public List<Book> filterBooksByStatus(String status) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getStatus().equals(status))
                .collect(Collectors.toList());
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

    public void deleteClientRating(String userName){
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

    public void deleteClientComments(String userName){
        commentRepository.deleteAllByClient_UserName(userName);
    }
}
