package com.bookstore.contoller;

import com.bookstore.model.Order;
import com.bookstore.util.OrderStatus;
import com.bookstore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@Secured("ROLE_ADMIN")
public class PanelController {

    private OrderRepository orderRepository;

    @Autowired
    public PanelController(OrderRepository repository) {
        this.orderRepository = repository;
    }

    @GetMapping("panel/zamowienia")
    public String getAllOrders(@RequestParam(required = false) OrderStatus status, Model model) {
        List<Order> orders;
        if (status == null) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findAllByOrderStatus(status);
        }
        model.addAttribute("orders", orders);
        return "panel";
    }

    @GetMapping("/panel/zamowienie/{id}")
    public String singleOrder(@PathVariable Long id, Model model) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(o -> singleOrderPanel(o, model))
                .orElse("redirect:/");
    }

    @PostMapping("/panel/zamowienie/{id}")
    public String changeOrderStatus(@PathVariable Long id, Model model) {
        Optional<Order> order = orderRepository.findById(id);
        order.ifPresent(o -> {
            o.setOrderStatus(OrderStatus.nextStatus(o.getOrderStatus()));
            orderRepository.save(o);
        });
        return order.map(o -> singleOrderPanel(o, model))
                .orElse("redirect:/");
    }

    private String singleOrderPanel(Order order, Model model) {
        model.addAttribute("order", order);
        return "status";
    }
}
