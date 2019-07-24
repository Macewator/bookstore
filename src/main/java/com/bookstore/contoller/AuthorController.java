package com.bookstore.contoller;

import com.bookstore.model.Book;
import com.bookstore.service.AuthorService;
import com.bookstore.service.ClientService;
import com.bookstore.util.Message;
import com.bookstore.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes({"status", "url", "entity_id", "entity_type", "books", "favorites", "favorites_isbn"})
public class AuthorController {

    private AuthorService authorService;
    private ClientService clientService;

    @Autowired
    public AuthorController(AuthorService service, ClientService clientService) {
        this.authorService = service;
        this.clientService = clientService;
    }

    @GetMapping("/authors")
    public String getAllAuthors(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        return "authors";
    }

    @GetMapping("authors/books")
    public String getAllBooksFromAuthor(@RequestParam Long entity_id, Model model) {
        model.addAttribute("books", authorService.getAllBooksFromAuthor(entity_id));
        model.addAttribute("url", "/authors");
        model.addAttribute("entity_id", entity_id);
        model.addAttribute("entity_type", Type.TYPE_AUTHOR);
        model.addAttribute("favorites", clientService.getAllFavorites());
        model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites());
        model.addAttribute("status", "empty");
        return "books_list";
    }

    @GetMapping("/authors/search")
    public String searchAuthor(@RequestParam String name, Model model) {
        model.addAttribute("authors", authorService.searchAuthor(name));
        return "authors";
    }

    @GetMapping("/authors/books/search")
    public String searchBooksFromAuthor(@RequestParam Long entity_id, @RequestParam String value,
                                        @RequestParam(defaultValue = "default") String category,
                                        @RequestParam String status, Model model) {
        if (value.isEmpty()) {
            model.addAttribute("message", new Message("Search", "wypełnij pole wyszukiwania"));
            return "books_list";
        } else if (category.equals("default")) {
            model.addAttribute("message", new Message("Search", "zaznacz kategorie wyszukiwania"));
            return "books_list";
        }
        model.addAttribute("books", authorService.searchBooksFromAuthor(entity_id, value, category, status));
        return "books_list";
    }

    @GetMapping("/authors/books/{status}")
    public String filterPublisherBooks(@PathVariable String status,
                                       @SessionAttribute(name = "entity_id", required = false) Long id,
                                       Model model) {
        if (id == null) {
            return "redirect:/authors";
        }
        model.addAttribute("books", authorService.filterAuthorBooksByStatus(status, id));
        model.addAttribute("status", status);
        return "books_list";
    }

    @GetMapping("/authors/books/sort")
    public String sortAuthorBooks(@RequestParam(defaultValue = "default") String type, @RequestParam String option,
                                  @SessionAttribute(name = "books", required = false) List<Book> books,
                                  Model model) {
        if (books == null) {
            return "redirect:/authors";
        }
        model.addAttribute("books", authorService.sortAuthorBooks(type, option, books));
        return "books_list";
    }
}
