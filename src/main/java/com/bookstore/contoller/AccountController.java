package com.bookstore.contoller;

import com.bookstore.model.Client;
import com.bookstore.model.ClientUpdateData;
import com.bookstore.model.CreditCard;
import com.bookstore.service.BookService;
import com.bookstore.service.ClientService;
import com.bookstore.service.OrderService;
import com.bookstore.util.ClientNotFoundException;
import com.bookstore.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@SessionAttributes({"user_name"})
public class AccountController {

    private ClientService clientService;
    private BookService bookService;
    private OrderService orderService;

    @Autowired
    public AccountController(ClientService clientService, BookService bookService, OrderService orderService) {
        this.clientService = clientService;
        this.bookService = bookService;
        this.orderService = orderService;
    }

    @GetMapping("/account/update")
    public String editClientData(Authentication auth, Model model) {
        try {
            model.addAttribute("user_name", auth.getName());
            model.addAttribute("client_upd", clientService.createClientUpdData(auth.getName()));
        } catch (ClientNotFoundException e) {
            model.addAttribute("exception", e.getMessage());
        }
        return "client_update";
    }

    @PostMapping("/account/update")
    public String updateClientData(@ModelAttribute("client_upd") @Valid ClientUpdateData clientUpdData,
                                   BindingResult bindingResult,
                                   @SessionAttribute(name = "user_name") String userName,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            return "client_update";
        } else {
            try {
                clientService.updateClient(clientUpdData, userName);
            } catch (ClientNotFoundException e){
                model.addAttribute("exception", e.getMessage());
                return "account";
            }catch (IllegalArgumentException e) {
                model.addAttribute("exception", e.getMessage());
                return "client_update";
            }
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/loginform";
    }

    @GetMapping("/account/delete")
    public String deleteClientAccount(Authentication auth, RedirectAttributes attr){
        String userName = auth.getName();
        try {
            orderService.checkClientOrdersStatus(userName);
        }catch (IllegalArgumentException e){
            attr.addFlashAttribute("client_info", true);
            attr.addFlashAttribute("exception", e.getMessage());
            return "redirect:/client/account";
        }
        bookService.deleteClientComments(userName);
        bookService.deleteClientRating(userName);
        clientService.deleteClientFavorites(userName);
        clientService.deleteClientAccount(userName);
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/";
    }

    @GetMapping("/password/update")
    public String editClientPassword(Model model) {
        model.addAttribute("password", false);
        return "password_update";
    }

    @PostMapping("/password/update")
    public String updateClientPassword(@RequestParam String oldPassword,
                                       @RequestParam String newPassword,
                                       Authentication auth, Model model) {
        try {
            Client client = clientService.findClientByUserName(auth.getName());
            clientService.checkAndUpdateClientPassword(client, oldPassword, newPassword);
        } catch (ClientNotFoundException e) {
            model.addAttribute("exception", e.getMessage());
            return "account";
        }catch (IllegalArgumentException e){
            model.addAttribute("exception", e.getMessage());
            return "password_update";
        }
        model.addAttribute("message", new Message("Password", "hasło zostało zmienione"));
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/loginform";
    }

    @GetMapping("/payment/update")
    public String updateClientPayment(Model model) {
        model.addAttribute("card", new CreditCard());
        model.addAttribute("update", true);
        return "payment";
    }

    @PostMapping("/payment/update")
    public String updateClientPaymentConfirm(@ModelAttribute("card") @Valid CreditCard card,
                                             BindingResult bindingResult,
                                             Authentication auth, Model model) {
        if (bindingResult.hasErrors()) {
            return "payment";
        }
        try {
            clientService.updateClientCreditCard(auth.getName(), card);
        }catch (ClientNotFoundException e){
            model.addAttribute("exception", e.getMessage());
            return "account";
        }
        model.addAttribute("credit_card", card);
        model.addAttribute("payment", true);
        return "account";
    }
}
