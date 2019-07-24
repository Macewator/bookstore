package com.bookstore.model;

import com.bookstore.util.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class ClientOrder {

    private Order order;

    public ClientOrder() {
        prepareNewOrder();
    }

    public Order getOrder() {
        return order;
    }

    public void add(Book book) {
        order.getBooks().add(book);
    }

    public void prepareNewOrder() {
        order = new Order();
        order.setStatus(OrderStatus.NEW);
    }

    public void clearCurrentOrder(){
        order.getBooks().removeAll(order.getBooks());
    }
}
