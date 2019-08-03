package com.bookstore.contoller;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.ClientService;
import com.bookstore.util.ClientNotFoundException;
import com.bookstore.util.Message;
import com.bookstore.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bookstore.util.Type.*;

@Controller
@SessionAttributes({"value", "category", "pageNumbers", "favorites", "favorites_isbn"})
public class BookController {

    private static final String DEFAULT_BOOK_STATUS = "default";

    private BookService bookService;
    private ClientService clientService;

    @Autowired
    public BookController(BookService bookService, ClientService clientService) {
        this.bookService = bookService;
        this.clientService = clientService;
    }

    @GetMapping("/books")
    public String getAllBooks(@RequestParam int page, Model model) {
        Page<Book> bookPage = bookService.getAllBooks(page);
        int totalPages = bookPage.getTotalPages();
        model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
        model.addAttribute("current_page", page);
        model.addAttribute("url", "/books");
        model.addAttribute("entity_type", TYPE_DEFAULT);
        model.addAttribute("status", DEFAULT_BOOK_STATUS);
        model.addAttribute("sort_type", "default");
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("favorites", clientService.getAllFavorites());
        model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites());
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
                            @RequestParam(defaultValue = "default") String status,
                            @RequestParam(defaultValue = "false") Boolean search,
                            @SessionAttribute(required = false) String value,
                            @SessionAttribute(required = false) String category,
                            @RequestParam(required = false) String sort_type,
                            @RequestParam int page, Model model) {
        if (search) {
            List<Book> sortedBooks = bookService.sortOptionsAfterSearch(type, option, value, category, status, page, sort_type);
            model.addAttribute("books", sortedBooks);
            model.addAttribute("search", search);
            model.addAttribute("sort_type", sort_type);
            model.addAttribute("url", "/books/sort?type=" + type + "&option=" + option + "&search=" + search + "&sort_type=" + sort_type);
        } else {
            Page<Book> bookPage = bookService.sortBooks(type, option, status, page);
            int totalPages = bookPage.getTotalPages();
            model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
            model.addAttribute("sort_type", "default");
            model.addAttribute("books", bookPage.getContent());
            model.addAttribute("url", "/books/sort?type=" + type + "&option=" + option);
        }
        model.addAttribute("current_page", page);
        model.addAttribute("sort", true);
        model.addAttribute("type", type);
        model.addAttribute("option", option);
        model.addAttribute("status", status);
        model.addAttribute("favorites", clientService.getAllFavorites());
        model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites());
        return "books";
    }

    @GetMapping("/books/search")
    public String searchBooks(@RequestParam String value,
                              @RequestParam(defaultValue = "default") String category,
                              @RequestParam(required = false) String status,
                              @RequestParam int page, Model model) {
        Page<Book> bookPage = bookService.searchBooks(value, category, status, page);
        int totalPages = bookPage.getTotalPages();
        model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
        model.addAttribute("current_page", page);
        model.addAttribute("url", "/books/search?value=" + value + "&category=" + category + "&status=" + status);
        model.addAttribute("search", true);
        model.addAttribute("status", status);
        model.addAttribute("sort_type", "default");
        model.addAttribute("value", value);
        model.addAttribute("category", category);
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("favorites", clientService.getAllFavorites());
        model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites());
        if (value.isEmpty()) {
            model.addAttribute("message", new Message("Search", "wypełnij pole wyszukiwania"));
            return "books";
        } else if (category.equals("default")) {
            model.addAttribute("message", new Message("Search", "zaznacz kategorie wyszukiwania"));
            return "books";
        }
        return "books";
    }

    @GetMapping("/books/filter/{status}")
    public String filterBooks(@PathVariable String status, @RequestParam int page, Model model) {
        Page<Book> bookPage = bookService.filterBooksByStatus(status, page);
        int totalPages = bookPage.getTotalPages();
        model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
        model.addAttribute("current_page", page);
        model.addAttribute("url", "/books/sort?status=" + status);
        model.addAttribute("entity_type", TYPE_STATUS);
        model.addAttribute("sort_type", "default");
        model.addAttribute("status", status);
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("favorites", clientService.getAllFavorites());
        model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites());
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
