package com.bookstore.service;

import com.bookstore.model.*;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.OrderRepository;
import com.bookstore.util.Message;
import com.bookstore.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private BookRepository bookRepository;
    private ClientOrder clientOrder;

    @Autowired
    public OrderService(OrderRepository orderRepository, BookRepository bookRepository, ClientOrder clientOrder) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.clientOrder = clientOrder;
    }

    public void addBookToOrder(Long isbn) {
        Optional<Book> book = bookRepository.findById(isbn);
        if(book.isPresent()){
            clientOrder.add(book.get());
        }else {
            throw new NoSuchElementException("książka nie istnieje");
        }
    }

    public Order getOrder() {
        return setOrderPrice(clientOrder.getOrder());
    }

    private Order setOrderPrice(Order order) {
        double orderPrice = order.getBooks().stream()
                .mapToDouble(Book::getPrice)
                .sum();
        order.setOrderPrice(orderPrice);
        return order;
    }

    public List<Order> getOrderHistory(String userName) {
        List<Order> orders = orderRepository.findAllByClient_UserName(userName);
        return historyOrderPrice(orders);
    }

    private List<Order> historyOrderPrice(List<Order> orders) {
        for (Order order : orders) {
            double orderPrice = order.getBooks().stream()
                    .mapToDouble(Book::getPrice)
                    .sum();
            order.setOrderPrice(orderPrice);
        }
        return orders;
    }

    public void confirmOrder(Client client, Address address) {
        Order order = clientOrder.getOrder();
        order.setClient(client);
        order.setOrderAddress(address);
        orderRepository.save(order);
        clientOrder.prepareNewOrder();
    }

    public void clearOrder() {
        clientOrder.clearCurrentOrder();
    }

    public void deleteClientCompletedOrders(OrderStatus status, String userName){
        orderRepository.deleteAllByOrderStatusAndClientUserName(status.toString(), userName);
    }

    public List<Order> findAllByStatus(OrderStatus orderStatus){
        return orderRepository.findAllByOrderStatus(orderStatus);
    }

    public void checkClientOrdersStatus(String userName) {
        List<Order> orders = orderRepository.findAllByClient_UserName(userName);
        for (Order o : orders) {
            if (checkOrderStatus(o)) {
                throw new IllegalArgumentException("nie możesz usnąć konta, posiadasz nie zakończone zamóweinia");
            }
        }
    }

    private boolean checkOrderStatus(Order order) {
        return order.getOrderStatus().equals(OrderStatus.NEW) || order.getOrderStatus().equals(OrderStatus.IN_PROGRESS);
    }
}
