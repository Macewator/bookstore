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

import static com.bookstore.util.Action.*;

import java.util.NoSuchElementException;

@Controller
public class OrderController {

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
                                 @RequestParam(defaultValue = "999") Long entity_id,
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
                                 @RequestParam(defaultValue = "999") Long entity_id,
                                 @RequestParam(required = false) String type,
                                 @RequestParam(required = false) String option,
                                 @RequestParam(defaultValue = "default") String value,
                                 @RequestParam(defaultValue = "default") String category,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(defaultValue = "false") Boolean search,
                                 @RequestParam(required = false) String sort_type,
                                 @RequestParam int page, RedirectAttributes attr) {
        try {
            orderService.addBookToOrder(isbn);
        } catch (NoSuchElementException e) {
            attr.addFlashAttribute("exception", e.getMessage());
            return clientService.redirectForAction(action, entity_id, type, option, value, category, status, search, sort_type, page);
        }
        attr.addFlashAttribute("message", new Message("Add", "książke dodano do zamówienia"));
        return clientService.redirectForAction(action, entity_id, type, option, value, category, status, search, sort_type, page);
    }

    @RequestMapping(value = "/order/{action}", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmOrder(@PathVariable String action, @ModelAttribute Address address,
                               Authentication client, Model model) {
        if (action.equals(ACTION_CONFIRM_ORDER.getAction())) {
            try {
                orderService.confirmOrder(clientService.findClientByUserName(client.getName()), address);
                model.addAttribute("message", new Message("Confirm", "Twoje zamówienie przkazano do realizacji"));
            } catch (ClientNotFoundException e) {
                model.addAttribute("exception", e.getMessage());
            }
        } else if (action.equals(ACTION_CLEAR_ORDER.getAction())) {
            orderService.clearOrder();
            model.addAttribute("message", new Message("Empty", "Twoje zamówienie jest puste"));
        }
        return "account";
    }
}
