package com.bookstore.contoller;

import com.bookstore.model.Client;
import com.bookstore.model.CreditCard;
import com.bookstore.service.ClientService;
import com.bookstore.service.CreditCardService;
import com.bookstore.util.ClientNotFoundException;
import com.bookstore.service.OrderService;
import com.bookstore.util.OrderStatus;
import com.bookstore.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Controller
@SessionAttributes({"edit", "client", "update"})
public class ClientController {

    private ClientService clientService;
    private OrderService orderService;
    private CreditCardService creditCardService;

    @Autowired
    public ClientController(ClientService clientService, OrderService orderService, CreditCardService creditCardService) {
        this.clientService = clientService;
        this.orderService = orderService;
        this.creditCardService = creditCardService;
    }

    @GetMapping("/loginform")
    public String loginForm() {
        return "login_form";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("client", new Client());
        return "register";
    }

    @PostMapping("/register")
    public String registerClient(@ModelAttribute @Valid Client client,
                                 BindingResult clientBindingResult,
                                 Model model) {
        if (clientBindingResult.hasErrors()) {
            return "register";
        } else {
            try {
                clientService.checkRegisterData(client);
            } catch (IllegalArgumentException e) {
                model.addAttribute("exception", e.getMessage());
                return "register";
            }
        }
        return "redirect:/register/payment";
    }

    @GetMapping("/register/payment")
    public String paymentForm(Model model) {
        model.addAttribute("card", new CreditCard());
        return "payment";
    }

    @PostMapping("/register/payment")
    public String registerPayment(@ModelAttribute("card") @Valid CreditCard card,
                                  BindingResult cardBindingResult,
                                  @SessionAttribute(name = "client") Client client,
                                  Model model) {
        if (cardBindingResult.hasErrors()) {
            return "payment";
        } else {
            try {
                creditCardService.checkRegisterPayment(card);
            } catch (IllegalArgumentException e) {
                model.addAttribute("exception", e.getMessage());
                return "payment";
            }
        }
        clientService.addWithDefaultRole(client, card);
        return "redirect:/";
    }

    @GetMapping("/client/account")
    public String clientAccount(Authentication auth, Model model) {
        String userName = auth.getName();
        try {
            model.addAttribute("client", clientService.findClientByUserName(userName));
            model.addAttribute("client_template", "info");
        } catch (ClientNotFoundException e) {
            model.addAttribute("exception", e.getMessage());
        }
        return "account";
    }

    @GetMapping("/client/order")
    public String clientOrder(Authentication auth, Model model) {
        String userName = auth.getName();
        model.addAttribute("order", orderService.getOrder());
        model.addAttribute("orderAddress", clientService.findClientAddress(userName));
        model.addAttribute("client_template", "order");
        return "account";
    }

    @GetMapping("/client/history")
    public String clientHistory(Authentication auth, Model model) {
        String userName = auth.getName();
        model.addAttribute("history", orderService.getOrderHistory(userName));
        model.addAttribute("client_template", "history");
        return "account";
    }

    @GetMapping("/client/payment")
    public String clientPayment(Authentication auth, Model model) {
        String userName = auth.getName();
        try {
            model.addAttribute("credit_card", clientService.findClientCreditCard(userName));
            model.addAttribute("client_template", "payment");
        } catch (ClientNotFoundException e) {
            model.addAttribute("exception", e.getMessage());
        }
        return "account";
    }

    @GetMapping("/client/favorites")
    public String clientFavorites(Authentication auth, Model model) {
        String userName = auth.getName();
        model.addAttribute("books", clientService.getAllClientFavorites(userName));
        model.addAttribute("client_template", "favorites");
        return "account";
    }

    @GetMapping("/client/favorites/add")
    public String addBookToFavorites(@RequestParam Long isbn,
                                     @RequestParam(required = false) Type entity_type,
                                     @RequestParam(required = false) Long entity_id,
                                     @RequestParam(required = false) String entity_url,
                                     @RequestParam(required = false) String status,
                                     @RequestParam(required = false) Integer page,
                                     Authentication auth, Model model) {
        try {
            clientService.addToFavorites(isbn, auth.getName());
        } catch (NoSuchElementException | ClientNotFoundException e) {
            model.addAttribute("exception", e.getMessage());
        }
        return clientService.redirectForEntityType(entity_type, isbn, entity_id, entity_url, status, page);
    }

    @GetMapping("/client/favorites/remove")
    public String removeBookFromFavorites(@RequestParam Long isbn,
                                          @RequestParam(required = false) Type entity_type,
                                          @RequestParam(required = false) Long entity_id,
                                          @RequestParam(required = false) String entity_url,
                                          @RequestParam(required = false) String status,
                                          @RequestParam(required = false) Integer page,
                                          Authentication auth, Model model) {
        model.addAttribute("books", clientService.removeFromFavorites(isbn, auth.getName()));
        if (entity_type != Type.TYPE_ACCOUNT) {
            return clientService.redirectForEntityType(entity_type, isbn, entity_id, entity_url, status, page);
        }
        return "redirect:/client/favorites";
    }

    @GetMapping("/client/favorites/{action}/add")
    public String addBookToFavoritesAfterAction(@PathVariable String action,
                                                @RequestParam Long isbn,
                                                @RequestParam(required = false) Long entity_id,
                                                @RequestParam(required = false) String type,
                                                @RequestParam(required = false) String entity_name,
                                                @RequestParam(required = false) String option,
                                                @RequestParam(required = false) String value,
                                                @RequestParam(required = false) String category,
                                                @RequestParam(required = false) String status,
                                                @RequestParam(required = false) Boolean search,
                                                @RequestParam(required = false) String sort_type,
                                                @RequestParam Integer page, RedirectAttributes attr,
                                                Authentication auth) {
        try {
            clientService.addToFavorites(isbn, auth.getName());
        } catch (NoSuchElementException | ClientNotFoundException e) {
            attr.addFlashAttribute("exception", e.getMessage());
        }
        return clientService.redirectForAction(action, entity_id, entity_name, type, option, value, category, status, search, sort_type, page);
    }

    @GetMapping("/client/favorites/{action}/remove")
    public String removeBookFromFavoritesAfterAction(@PathVariable String action,
                                                     @RequestParam Long isbn,
                                                     @RequestParam(required = false) Long entity_id,
                                                     @RequestParam(required = false) String type,
                                                     @RequestParam(required = false) String entity_name,
                                                     @RequestParam(required = false) String option,
                                                     @RequestParam(required = false) String value,
                                                     @RequestParam(required = false) String category,
                                                     @RequestParam(required = false) String status,
                                                     @RequestParam(required = false) Boolean search,
                                                     @RequestParam(required = false) String sort_type,
                                                     @RequestParam Integer page, Authentication auth) {
        clientService.removeFromFavorites(isbn, auth.getName());
        return clientService.redirectForAction(action, entity_id, entity_name, type, option, value, category, status, search, sort_type, page);
    }


    @GetMapping("/client/history/delete")
    public String manageClientHistory(@RequestParam OrderStatus status, Authentication auth) {
        orderService.deleteClientCompletedOrders(status, auth.getName());
        return "redirect:/client/history";
    }

    @GetMapping("client/history/filter")
    public String getAllOrders(@RequestParam(required = false) OrderStatus status,
                               Authentication auth, Model model) {
        if (status == null) {
            model.addAttribute("history", orderService.getOrderHistory(auth.getName()));
        } else {
            model.addAttribute("history", orderService.findAllByStatus(status));
        }
        model.addAttribute("client_history", true);
        model.addAttribute("client_template", "history");
        return "account";
    }
}
