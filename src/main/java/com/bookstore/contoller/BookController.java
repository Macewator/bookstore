package com.bookstore.contoller;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.ClientService;
import com.bookstore.util.ClientNotFoundException;
import com.bookstore.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@SessionAttributes({"books","status", "favorites", "favorites_isbn"})
public class BookController {

    private static final String STATUS_EMPTY = "empty";

    private BookService bookService;
    private ClientService clientService;

    @Autowired
    public BookController(BookService bookService, ClientService clientService) {
        this.bookService = bookService;
        this.clientService = clientService;
    }

    @GetMapping("/books")
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("favorites", clientService.getAllFavorites());
        model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites());
        model.addAttribute("status", "empty");
        return "books";
    }

    @GetMapping("/books/details")
    public String bookDetails(@RequestParam Long isbn, Model model) {
        try {
            model.addAttribute("book", bookService.getBook(isbn));
        } catch (NoSuchElementException e) {
            model.addAttribute("exception", e.getMessage());
        }
        model.addAttribute("comments", bookService.getAllBookComments(isbn));
        model.addAttribute("rating", bookService.calculateBookRating(isbn));
        model.addAttribute("favorites", clientService.getAllFavorites());
        model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites());
        return "details";
    }

    @GetMapping("/books/sort")
    public String sortBooks(@RequestParam(defaultValue = "default") String type,
                            @RequestParam String option,
                            @SessionAttribute(name = "books", required = false) List<Book> books,
                            Model model) {
        if (books == null) {
            return "redirect:/books";
        }
        model.addAttribute("books", bookService.sortBooks(type, option, books));
        model.addAttribute("status", STATUS_EMPTY);
        return "books";
    }

    @GetMapping("/books/search")
    public String searchBooks(@RequestParam String value,
                              @RequestParam(defaultValue = "default") String category,
                              @RequestParam String status,
                              Model model) {
        if (value.isEmpty()) {
            model.addAttribute("message", new Message("Search", "wypełnij pole wyszukiwania"));
            return "books";
        } else if (category.equals("default")) {
            model.addAttribute("message", new Message("Search", "zaznacz kategorie wyszukiwania"));
            return "books";
        }
        model.addAttribute("books", bookService.searchBooks(value, category, status));
        return "books";
    }

    @GetMapping("/books/filter/{status}")
    public String filterBooks(@PathVariable String status, Model model) {
        model.addAttribute("books", bookService.filterBooksByStatus(status));
        model.addAttribute("status", status);
        return "books";
    }

    @GetMapping("/books/rating")
    public String bookRating(@RequestParam Long isbn,
                             @RequestParam String rating,
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
        model.addAttribute("comments", bookService.getAllBookComments(isbn));
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
