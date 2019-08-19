package com.bookstore.contoller;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.ClientService;
import com.bookstore.util.ClientNotFoundException;
import com.bookstore.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.linkbuilder.StandardLinkBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.bookstore.util.Type.*;

@Controller
@SessionAttributes({"value", "category", "pageNumbers", "favorites", "favorites_isbn"})
public class BookController {

    private static final String DEFAULT_VALUE = "default";

    private BookService bookService;
    private ClientService clientService;

    @Autowired
    public BookController(BookService bookService, ClientService clientService) {
        this.bookService = bookService;
        this.clientService = clientService;
    }

    @GetMapping("/books")
    public String getAllBooks(@RequestParam int page, Authentication auth, Model model) {
        Page<Book> bookPage = bookService.getAllBooks(page);
        int totalPages = bookPage.getTotalPages();
        model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
        model.addAttribute("current_page", page);
        model.addAttribute("url", "/books");
        model.addAttribute("entity_type", TYPE_DEFAULT);
        model.addAttribute("status", DEFAULT_VALUE);
        model.addAttribute("sort_type", DEFAULT_VALUE);
        model.addAttribute("books", bookPage.getContent());
        if (auth != null) {
            model.addAttribute("favorites", clientService.getAllFavorites());
            model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites(auth.getName()));
        }
        return "books";
    }

    @GetMapping("/books/details")
    public String bookDetails(@RequestParam Long isbn, Authentication auth, Model model) {
        try {
            model.addAttribute("book", bookService.getBook(isbn));
        } catch (NoSuchElementException e) {
            model.addAttribute("exception", e.getMessage());
        }
        model.addAttribute("comments", bookService.getAllBookComments(isbn));
        model.addAttribute("rating", bookService.calculateBookRating(isbn));
        if (auth != null) {
            model.addAttribute("favorites", clientService.getAllFavorites());
            model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites(auth.getName()));
        }
        return "details";
    }

    @GetMapping("/books/sort")
    public String sortBooks(@RequestParam(defaultValue = DEFAULT_VALUE) String type,
                            @RequestParam String option,
                            @RequestParam(defaultValue = DEFAULT_VALUE) String status,
                            @RequestParam(required = false) Boolean search,
                            @SessionAttribute(required = false) String value,
                            @SessionAttribute(required = false) String category,
                            @RequestParam(required = false) String sort_type,
                            @RequestParam int page, Authentication auth, Model model) {
        if (search != null) {
            List<Book> sortedBooks = bookService.sortOptionsAfterSearch(type, option, value, category, status, page, sort_type);
            model.addAttribute("books", sortedBooks);
            model.addAttribute("search", search);
            model.addAttribute("sort_type", sort_type);
            model.addAttribute("url", "/books/sort?type=" + type + "&option=" + option + "&search=" + search + "&sort_type=" + sort_type);
        } else {
            Page<Book> bookPage = bookService.sortBooks(type, option, status, page);
            int totalPages = bookPage.getTotalPages();
            model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
            model.addAttribute("sort_type", DEFAULT_VALUE);
            model.addAttribute("books", bookPage.getContent());
            model.addAttribute("url", "/books/sort?type=" + type + "&option=" + option);
        }
        model.addAttribute("current_page", page);
        model.addAttribute("sort", true);
        model.addAttribute("type", type);
        model.addAttribute("option", option);
        model.addAttribute("status", status);
        if (auth != null) {
            model.addAttribute("favorites", clientService.getAllFavorites());
            model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites(auth.getName()));
        }
        return "books";
    }

    @GetMapping("/books/search")
    public String searchBooks(@RequestParam String value,
                              @RequestParam(defaultValue = DEFAULT_VALUE) String category,
                              @RequestParam(required = false) String status,
                              @RequestParam int page, Authentication auth, Model model) {
        Page<Book> bookPage = bookService.searchBooks(value, category, status, page);
        int totalPages = bookPage.getTotalPages();
        model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
        model.addAttribute("current_page", page);
        model.addAttribute("url", "/books/search?value=" + value + "&category=" + category + "&status=" + status);
        model.addAttribute("search", true);
        model.addAttribute("status", status);
        model.addAttribute("sort_type", DEFAULT_VALUE);
        model.addAttribute("value", value);
        model.addAttribute("category", category);
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("favorites", clientService.getAllFavorites());
        if (auth != null) {
            model.addAttribute("favorites", clientService.getAllFavorites());
            model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites(auth.getName()));
        }
        if (value.isEmpty()) {
            model.addAttribute("message", new Message("Search", "wypełnij pole wyszukiwania"));
            return "books";
        } else if (category.equals("default")) {
            model.addAttribute("message", new Message("Search", "zaznacz kategorie wyszukiwania"));
            return "books";
        }
        return "books";
    }

    @GetMapping("/books/{status}")
    public String filterBooks(@PathVariable String status, @RequestParam int page,
                              Authentication auth, Model model) {
        Page<Book> bookPage = bookService.filterBooksByStatus(status, page);
        int totalPages = bookPage.getTotalPages();
        model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
        model.addAttribute("current_page", page);
        model.addAttribute("url", "/books/sort?status=" + status);
        model.addAttribute("entity_type", TYPE_STATUS);
        model.addAttribute("sort_type", DEFAULT_VALUE);
        model.addAttribute("status", status);
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("favorites", clientService.getAllFavorites());
        if (auth != null) {
            model.addAttribute("favorites", clientService.getAllFavorites());
            model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites(auth.getName()));
        }
        return "books";
    }

    @GetMapping("/books/rating")
    public String bookRating(@RequestParam Long isbn, @RequestParam String rating,
                             Authentication auth, Model model) {
        if (bookService.ratingCheck(auth.getName(), isbn)) {
            try {
                model.addAttribute("book", bookService.getBook(isbn));
                model.addAttribute("message", new Message("Rating", "dodałeś już ocene"));
            } catch (NoSuchElementException e) {
                model.addAttribute("exception", e.getMessage());
            }
        } else {
            try {
                model.addAttribute("book", bookService.bookRating(isbn, rating, auth.getName()));
            } catch (ClientNotFoundException | NoSuchElementException e) {
                model.addAttribute("exception", e.getMessage());
            }
        }
        /*model.addAttribute("comments", bookService.getAllBookComments(isbn));*/
        model.addAttribute("rating", bookService.calculateBookRating(isbn));
        return "details";
    }

    @PostMapping("/books/comments")
    public String bookComments(@RequestParam String comment,
                               @RequestParam Long isbn,
                               Authentication auth, Model model) {
        try {
            model.addAttribute("book", bookService.addComment(comment, auth.getName(), isbn));
        } catch (ClientNotFoundException | NoSuchElementException e) {
            model.addAttribute("exception", e.getMessage());
        }
        model.addAttribute("comments", bookService.getAllBookComments(isbn));
        return "details";
    }
}
