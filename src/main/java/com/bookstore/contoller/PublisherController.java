package com.bookstore.contoller;

import com.bookstore.model.Book;
import com.bookstore.service.ClientService;
import com.bookstore.util.Message;
import com.bookstore.service.PublisherService;
import com.bookstore.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes({"status", "url", "entity_id", "entity_type", "books", "favorites", "favorites_isbn"})
public class PublisherController {

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
    public String getAllBooksFromPublisher(@RequestParam Long entity_id, Model model) {
        model.addAttribute("books", publisherService.getAllBooksFromPublisher(entity_id));
        model.addAttribute("url", "/publishers");
        model.addAttribute("entity_id", entity_id);
        model.addAttribute("entity_type", Type.TYPE_PUBLISHER);
        model.addAttribute("favorites", clientService.getAllFavorites());
        model.addAttribute("favorites_isbn", clientService.getAllBookIsbnFromFavorites());
        model.addAttribute("status", "empty");
        return "books_list";
    }

    @GetMapping("/publishers/search")
    public String searchAuthor(@RequestParam String name, Model model) {
        model.addAttribute("publishers", publisherService.searchPublisher(name));
        return "publishers";
    }

    @GetMapping("/publishers/books/search")
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
        model.addAttribute("books", publisherService.searchBooksFromPublisher(entity_id, value, category, status));
        return "books_list";
    }

    @GetMapping("/publishers/books/{status}")
    public String filterPublisherBooks(@PathVariable String status,
                                       @SessionAttribute(name = "entity_id", required = false) Long id,
                                       Model model) {
        if (id == null) {
            return "redirect:/publishers";
        }
        model.addAttribute("books", publisherService.filterPublisherBooksByStatus(status, id));
        model.addAttribute("status", status);
        return "books_list";
    }

    @GetMapping("/publishers/books/sort")
    public String sortPublisherBooks(@RequestParam(defaultValue = "default") String type, @RequestParam String option,
                                     @SessionAttribute(name = "books", required = false) List<Book> books,
                                     Model model) {
        if (books == null) {
            return "redirect:/publishers";
        }
        model.addAttribute("books", publisherService.sortPublisherBooks(type, option, books));
        return "books_list";
    }
}
