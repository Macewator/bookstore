package com.bookstore.contoller;

import com.bookstore.model.Address;
import com.bookstore.service.ClientService;
import com.bookstore.util.ClientNotFoundException;
import com.bookstore.util.Message;
import com.bookstore.service.OrderService;
import com.bookstore.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {

    private static final String CONFIRM_ORDER = "confirm";
    private static final String CLEAR_ORDER = "clear";

    private OrderService orderService;
    private ClientService clientService;

    @Autowired
    public OrderController(OrderService orderService, ClientService clientService) {
        this.orderService = orderService;
        this.clientService = clientService;
    }

    @GetMapping("/order/add")
    public String addBookToOrder(@RequestParam Long isbn, @RequestParam(required = false) Type entity_type,
                                 @RequestParam(required = false) Long entity_id, Model model) {
        model.addAttribute("book", orderService.addBookToOrder(isbn));
        model.addAttribute("message", new Message("Add", "książke dodano do zamówienia"));
        return clientService.redirectForEntityType(entity_type, isbn, entity_id);
    }

    @RequestMapping(value = "/order/{process}", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmOrder(@PathVariable String process, @ModelAttribute Address address,
                               Authentication client, Model model) {
        if (process.equals(CONFIRM_ORDER)) {
            try {
                orderService.confirmOrder(clientService.findClientByUserName(client.getName()), address);
                model.addAttribute("message", new Message("Confirm", "Twoje zamówienie przkazano do realizacji"));
            } catch (ClientNotFoundException e) {
                model.addAttribute("exception", e.getMessage());
            }
        } else if (process.equals(CLEAR_ORDER)) {
            orderService.clearOrder();
            model.addAttribute("message", new Message("Empty", "Twoje zamówienie jest puste"));
        }
        return "account";
    }
}
