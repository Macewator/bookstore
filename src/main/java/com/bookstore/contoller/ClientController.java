package com.bookstore.contoller;

import com.bookstore.model.Client;
import com.bookstore.model.CreditCard;
import com.bookstore.service.ClientService;
import com.bookstore.service.CreditCardService;
import com.bookstore.util.ClientNotFoundException;
import com.bookstore.util.Message;
import com.bookstore.service.OrderService;
import com.bookstore.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

import static com.bookstore.util.Option.*;

@Controller
@SessionAttributes({"edit", "client", "update"})
public class ClientController {

    private static final String ACTION_ADD = "add";
    private static final String ACTION_REMOVE = "remove";

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
        model.addAttribute("card", new CreditCard());
        return "payment";
    }

    @PostMapping("/register/payment")
    public String registerPayment(@ModelAttribute @Valid CreditCard card,
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

    @GetMapping("/client/{option}")
    public String clientAccount(@PathVariable String option, Authentication auth, Model model) {
        String userName = auth.getName();
        switch (getSelectedOption(option)) {
            case OPTION_ACCOUNT:
                try {
                    model.addAttribute("client", clientService.findClientByUserName(userName));
                    model.addAttribute("client_info", true);
                } catch (ClientNotFoundException e) {
                    model.addAttribute("exception", e.getMessage());
                }
                break;
            case OPTION_ORDER:
                model.addAttribute("order", orderService.getOrder());
                model.addAttribute("orderAddress", clientService.findClientAddress(userName));
                model.addAttribute("client_order", true);
                break;
            case OPTION_HISTORY:
                model.addAttribute("history", orderService.getOrderHistory(userName));
                model.addAttribute("client_history", true);
                break;
            case OPTION_PAYMENT:
                try {
                    model.addAttribute("credit_card", clientService.findClientCreditCard(userName));
                    model.addAttribute("payment", true);
                } catch (ClientNotFoundException e) {
                    model.addAttribute("exception", e.getMessage());
                }
                break;
            case OPTION_FAVORITES:
                model.addAttribute("books", clientService.getAllClientFavorites(userName));
                model.addAttribute("favorites", true);
                break;
            default:
                model.addAttribute("message", new Message("Option", "brak żądanej opcji"));
        }
        return "account";
    }

    @GetMapping("/client/favorites/{action}")
    public String manageClientFavorites(@PathVariable String action, @RequestParam Long isbn,
                                        @RequestParam(required = false) Type entity_type,
                                        @RequestParam(required = false) Long entity_id,
                                        Authentication auth, Model model) {
        if (action.equals(ACTION_ADD)) {
            try {
                clientService.addToFavorites(isbn, auth.getName());
            } catch (NoSuchElementException | ClientNotFoundException e) {
                model.addAttribute("exception", e.getMessage());
            }
            return clientService.redirectForEntityType(entity_type, isbn, entity_id);
        } else if (action.equals(ACTION_REMOVE)) {
            model.addAttribute("books", clientService.removeFromFavorites(isbn, auth.getName()));
            if (entity_type != Type.TYPE_ACCOUNT) {
                return clientService.redirectForEntityType(entity_type, isbn, entity_id);
            }
            return "redirect:/client/favorites";
        }
        return "redirect:/";
    }
}
