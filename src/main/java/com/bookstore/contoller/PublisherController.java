package com.bookstore.contoller;

import com.bookstore.model.Book;
import com.bookstore.service.ClientService;
import com.bookstore.util.Message;
import com.bookstore.service.PublisherService;
import com.bookstore.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bookstore.util.Type.*;

@Controller
@SessionAttributes({"pageNumbers", "value", "category", "entity_url",
        "entity_id", "entity_type"})
public class PublisherController {

    private static final String DEFAULT_VALUE = "default";

    private PublisherService publisherService;
    private ClientService clientService;

    @Autowired
    public PublisherController(PublisherService service, ClientService clientService) {
        this.publisherService = service;
        this.clientService = clientService;
    }

    @GetMapping("/publishers")
    public String getAllAuthors(Model model) {
        model.addAttribute("publishers", publisherService.getAllPublishers());
        return "publishers";
    }

    @GetMapping("publishers/books")
    public String getAllBooksFromPublisher(@RequestParam Long entity_id, @RequestParam int page,
                                           Authentication auth, Model model) {
        Page<Book> bookPage = publisherService.getAllBooksFromPublisher(entity_id, page);
        int totalPages = bookPage.getTotalPages();
        model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
        model.addAttribute("current_page", page);
        model.addAttribute("entity_url", "publishers");
        model.addAttribute("entity_id", entity_id);
        model.addAttribute("entity_type", TYPE_PUBLISHER);
        model.addAttribute("status", DEFAULT_VALUE);
        model.addAttribute("sort_type", DEFAULT_VALUE);
        model.addAttribute("books", bookPage.getContent());
        if (auth != null) {
            model.addAttribute("favorites", clientService.getAllFavorites());
            model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites(auth.getName()));
        }
        return "books_list";
    }

    @GetMapping("/publishers/search")
    public String searchAuthor(@RequestParam String name, Model model) {
        model.addAttribute("publishers", publisherService.searchPublisher(name));
        return "publishers";
    }

    @GetMapping("/publishers/books/search")
    public String searchBooksFromAuthor(@RequestParam Long entity_id,
                                        @RequestParam String value,
                                        @RequestParam(defaultValue = "default") String category,
                                        @RequestParam(required = false) String status,
                                        @RequestParam int page, Authentication auth, Model model) {
        Page<Book> bookPage = publisherService.searchBooksFromPublisher(entity_id, value, category, status, page);
        int totalPages = bookPage.getTotalPages();
        model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
        model.addAttribute("current_page", page);
        model.addAttribute("url", "publishers/books/search?value=" + value + "&category=" + category + "&status=" + status);
        model.addAttribute("search", true);
        model.addAttribute("status", status);
        model.addAttribute("sort_type", DEFAULT_VALUE);
        model.addAttribute("value", value);
        model.addAttribute("category", category);
        model.addAttribute("books", bookPage.getContent());
        if (auth != null) {
            model.addAttribute("favorites", clientService.getAllFavorites());
            model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites(auth.getName()));
        }
        if (value.isEmpty()) {
            model.addAttribute("message", new Message("Search", "wypełnij pole wyszukiwania"));
            return "books_list";
        } else if (category.equals("default")) {
            model.addAttribute("message", new Message("Search", "zaznacz kategorie wyszukiwania"));
            return "books_list";
        }
        return "books_list";
    }

    @GetMapping("/publishers/books/sort")
    public String sortPublisherBooks(@SessionAttribute Long entity_id,
                                     @RequestParam(defaultValue = DEFAULT_VALUE) String type,
                                     @RequestParam String option,
                                     @RequestParam(defaultValue = DEFAULT_VALUE) String status,
                                     @RequestParam(defaultValue = "false") Boolean search,
                                     @SessionAttribute(required = false) String value,
                                     @SessionAttribute(required = false) String category,
                                     @RequestParam(required = false) String sort_type,
                                     @RequestParam int page, Authentication auth, Model model) {
        if (search) {
            List<Book> sortedBooks = publisherService.sortOptionsAfterSearch(entity_id, type, option, value, category, status, page, sort_type);
            model.addAttribute("books", sortedBooks);
            model.addAttribute("search", search);
            model.addAttribute("sort_type", sort_type);
            model.addAttribute("url", "/publishers/books/sort?type=" + type + "&option=" + option + "&search=" + search + "&sort_type=" + sort_type);
        } else {
            Page<Book> bookPage = publisherService.sortPublisherBooks(entity_id, type, option, status, page);
            int totalPages = bookPage.getTotalPages();
            model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
            model.addAttribute("sort_type", DEFAULT_VALUE);
            model.addAttribute("books", bookPage.getContent());
            model.addAttribute("url", "/publishers/books/sort?type=" + type + "&option=" + option);
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
        return "books_list";
    }

    @GetMapping("/publishers/books/{status}")
    public String filterPublisherBooks(@PathVariable String status,
                                       @SessionAttribute(name = "entity_id", required = false) Long id,
                                       @RequestParam int page, Authentication auth, Model model) {
        if (id == null) {
            return "redirect:/authors";
        }
        Page<Book> bookPage = publisherService.filterPublisherBooksByStatus(id, status, page);
        int totalPages = bookPage.getTotalPages();
        model.addAttribute("pageNumbers", clientService.pageCounter(totalPages));
        model.addAttribute("current_page", page);
        model.addAttribute("url", "/publishers/books/sort?status=" + status);
        model.addAttribute("entity_type", TYPE_STATUS);
        model.addAttribute("sort_type", DEFAULT_VALUE);
        model.addAttribute("status", status);
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("favorites", clientService.getAllFavorites());
        if (auth != null) {
            model.addAttribute("favorites", clientService.getAllFavorites());
            model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites(auth.getName()));
        }
        return "books_list";
    }
}
