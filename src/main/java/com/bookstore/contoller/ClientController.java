package com.bookstore.contoller;

import com.bookstore.model.Client;
import com.bookstore.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ClientController {

    private ClientService service;

    @Autowired
    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("client", new Client());
        return "register";
    }

    @PostMapping("/register")
    public String registerClient(@ModelAttribute @Valid Client client,
                                 BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "register";
        }else {
            service.addWithDefaultRole(client);
            return "success";
        }
    }

    @GetMapping("/smt")
    public String secret(){
        return "smt";
    }
}
