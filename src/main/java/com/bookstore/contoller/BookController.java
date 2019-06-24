package com.bookstore.contoller;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookController {

    private BookService service;

    @Autowired
    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping("/books")
    public String getAllBooks(Model model){
        model.addAttribute("books", service.getAllBooks());
        return "books";
    }

    @GetMapping("/books/details")
    public String bookDetails(@RequestParam Long isbn, Model model ){
        model.addAttribute("details", service.getBookDetails(isbn));
        return "details";
    }

    @GetMapping("/books/sort")
    public String sortBooks(@RequestParam(defaultValue = "default") String type, Model model){
        model.addAttribute("books", service.sortBooks(type));
        return "books";
    }

    @GetMapping("/books/search")
    public String searchBooks(@RequestParam String value,
                              @RequestParam(defaultValue = "default") String category, Model model){
        if(value.isEmpty()){
            model.addAttribute("alert", "wypełnij pole wyszukiwania");
            return "books";
        }else if(category.equals("default")){
            model.addAttribute("alert", "zaznacz kategorie wyszukiwania");
            return "books";
        }
        model.addAttribute("books", service.searchBooks(value, category));
        return "books";
    }
}
