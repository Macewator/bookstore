package com.bookstore.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class ClientOrder {

    private Order order;

    public ClientOrder() {
        clear();
    }

    public Order getOrder() {
        return order;
    }

    public void add(Book book) {
        order.getBooks().add(book);
    }

    public void clear() {
        order = new Order();
        order.setOrderStatus(OrderStatus.NEW);
    }
}
