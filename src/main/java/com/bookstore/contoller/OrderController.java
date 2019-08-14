package com.bookstore.contoller;

import com.bookstore.model.Address;
import com.bookstore.service.ClientService;
import com.bookstore.util.ClientNotFoundException;
import com.bookstore.util.Message;
import com.bookstore.service.OrderService;
import com.bookstore.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
public class OrderController {

    private static final String DEFAULT_VALUE = "default";

    private OrderService orderService;
    private ClientService clientService;

    @Autowired
    public OrderController(OrderService orderService, ClientService clientService) {
        this.orderService = orderService;
        this.clientService = clientService;
    }

    @GetMapping("/order/add")
    public String addBookToOrder(@RequestParam Long isbn,
                                 @RequestParam(required = false) Type entity_type,
                                 @RequestParam(required = false) Long entity_id,
                                 @RequestParam(required = false) String entity_url,
                                 @RequestParam(required = false) String status,
                                 @RequestParam int page, RedirectAttributes attr) {
        try {
            orderService.addBookToOrder(isbn);
        } catch (NoSuchElementException e) {
            attr.addFlashAttribute("exception", e.getMessage());
            return clientService.redirectForEntityType(entity_type, isbn, entity_id, entity_url, status, page);
        }
        attr.addFlashAttribute("message", new Message("Add", "książke dodano do zamówienia"));
        return clientService.redirectForEntityType(entity_type, isbn, entity_id, entity_url, status, page);
    }

    @GetMapping("/order/{action}/add")
    public String addBookToOrder(@PathVariable(required = false) String action,
                                 @RequestParam Long isbn,
                                 @RequestParam(required = false) Long entity_id,
                                 @RequestParam(required = false) String entity_name,
                                 @RequestParam(required = false) String type,
                                 @RequestParam(required = false) String option,
                                 @RequestParam(defaultValue = DEFAULT_VALUE) String value,
                                 @RequestParam(defaultValue = DEFAULT_VALUE) String category,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(required = false) Boolean search,
                                 @RequestParam(required = false) String sort_type,
                                 @RequestParam int page, RedirectAttributes attr) {
        try {
            orderService.addBookToOrder(isbn);
        } catch (NoSuchElementException e) {
            attr.addFlashAttribute("exception", e.getMessage());
            return clientService.redirectForAction(action, entity_id, entity_name, type,  option, value, category, status, search, sort_type, page);
        }
        attr.addFlashAttribute("message", new Message("Add", "książke dodano do zamówienia"));
        return clientService.redirectForAction(action, entity_id, entity_name, type, option, value, category, status, search, sort_type, page);
    }

    @PostMapping("/order/confirm")
    public String confirmOrder(@ModelAttribute Address address, Authentication auth, Model model) {
        try {
            orderService.confirmOrder(clientService.findClientByUserName(auth.getName()), address);
            model.addAttribute("message", new Message("Confirm", "Twoje zamówienie przkazano do realizacji"));
        } catch (ClientNotFoundException e) {
            model.addAttribute("exception", e.getMessage());
        }
        return "account";
    }

    @GetMapping("/order/clear")
    public String clearOrder(@ModelAttribute Address address, Model model) {
        orderService.clearOrder();
        model.addAttribute("client_template", "order");
        model.addAttribute("message", new Message("Empty", "Twoje zamówienie jest puste"));
        return "account";
    }
}
