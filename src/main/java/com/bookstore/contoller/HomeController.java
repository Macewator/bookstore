package com.bookstore.contoller;

import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private BookRepository repository;

    @Autowired
    public void setRepository(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String home(Model model){
        Book book = repository.findRandomBook();
        List<Book> newBooks = repository.findAllByStatus("new");
        List<Book> bestBooks = repository.findAllByStatus("best");
        List<Book> regBooks = repository.findAllByStatus("reg");
        model.addAttribute("book", book);
        model.addAttribute("newBook", newBooks);
        model.addAttribute("bestBooks", bestBooks);
        model.addAttribute("regBooks", regBooks);
        return "home";
    }
}
