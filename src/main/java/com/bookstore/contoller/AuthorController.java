package com.bookstore.contoller;

import com.bookstore.model.Book;
import com.bookstore.service.AuthorService;
import com.bookstore.service.ClientService;
import com.bookstore.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bookstore.util.Type.*;

@Controller
@SessionAttributes({"pageNumbers", "value", "category", "entity_url",
        "entity_id", "entity_type", "form_option", "favorites", "favorites_isbn"})
public class AuthorController {

    private static final String DEFAULT_BOOK_STATUS = "default";

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
    public String getAllBooksFromAuthor(@RequestParam Long entity_id, @RequestParam int page, Model model) {
        Page<Book> bookPage = authorService.getAllBooksFromAuthor(entity_id, page);
        int totalPages = bookPage.getTotalPages();
        model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
        model.addAttribute("current_page", page);
        model.addAttribute("entity_url", "authors");
        model.addAttribute("entity_id", entity_id);
        model.addAttribute("entity_type", TYPE_AUTHOR);
        model.addAttribute("status", DEFAULT_BOOK_STATUS);
        model.addAttribute("form_option", TYPE_AUTHOR);
        model.addAttribute("sort_type", "default");
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("favorites", clientService.getAllFavorites());
        model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites());
        return "books_list";
    }

    @GetMapping("/authors/search")
    public String searchAuthor(@RequestParam String name, Model model) {
        model.addAttribute("authors", authorService.searchAuthor(name));
        return "authors";
    }

    @GetMapping("/authors/books/search")
    public String searchBooksFromAuthor(@RequestParam Long entity_id,
                                        @RequestParam String value,
                                        @RequestParam(defaultValue = "default") String category,
                                        @RequestParam(required = false) String status,
                                        @RequestParam int page, Model model) {
        Page<Book> bookPage = authorService.searchBooksFromAuthor(entity_id, value, category, status, page);
        int totalPages = bookPage.getTotalPages();
        model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
        model.addAttribute("current_page", page);
        model.addAttribute("url", "authors/books/search?value=" + value + "&category=" + category + "&status=" + status);
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
            return "books_list";
        } else if (category.equals("default")) {
            model.addAttribute("message", new Message("Search", "zaznacz kategorie wyszukiwania"));
            return "books_list";
        }
        return "books_list";
    }

    @GetMapping("/authors/books/sort")
    public String sortAuthorBooks(@SessionAttribute Long entity_id,
                                  @RequestParam(defaultValue = "default") String type,
                                  @RequestParam String option,
                                  @RequestParam(defaultValue = "default") String status,
                                  @RequestParam(defaultValue = "false") Boolean search,
                                  @SessionAttribute(required = false) String value,
                                  @SessionAttribute(required = false) String category,
                                  @RequestParam(required = false) String sort_type,
                                  @RequestParam int page, Model model) {
        if (search) {
            List<Book> sortedBooks = authorService.sortOptionsAfterSearch(entity_id, type, option, value, category, status, page, sort_type);
            model.addAttribute("books", sortedBooks);
            model.addAttribute("search", search);
            model.addAttribute("sort_type", sort_type);
            model.addAttribute("url", "/authors/books/sort?type=" + type + "&option=" + option + "&search=" + search + "&sort_type=" + sort_type);
        } else {
            Page<Book> bookPage = authorService.sortAuthorBooks(entity_id, type, option, status, page);
            int totalPages = bookPage.getTotalPages();
            model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
            model.addAttribute("sort_type", "default");
            model.addAttribute("books", bookPage.getContent());
            model.addAttribute("url", "/authors/books/sort?type=" + type + "&option=" + option);
        }
        model.addAttribute("current_page", page);
        model.addAttribute("sort", true);
        model.addAttribute("type", type);
        model.addAttribute("option", option);
        model.addAttribute("status", status);
        model.addAttribute("favorites", clientService.getAllFavorites());
        model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites());
        return "books_list";
    }

    @GetMapping("/authors/books/{status}")
    public String filterPublisherBooks(@PathVariable String status,
                                       @SessionAttribute(name = "entity_id", required = false) Long id,
                                       @RequestParam int page, Model model) {
        if (id == null) {
            return "redirect:/authors";
        }
        Page<Book> bookPage = authorService.filterAuthorBooksByStatus(id, status, page);
        int totalPages = bookPage.getTotalPages();
        model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
        model.addAttribute("current_page", page);
        model.addAttribute("url", "/authors/books/sort?status=" + status);
        model.addAttribute("entity_type", TYPE_STATUS);
        model.addAttribute("sort_type", "default");
        model.addAttribute("status", status);
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("favorites", clientService.getAllFavorites());
        model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites());
        return "books_list";
    }
}
