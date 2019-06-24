package com.bookstore.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Long id;

    @Enumerated
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id_user_name")
    private Client client;

    @ManyToMany
    @JoinTable(name = "ordered_books",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id_order"),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id_book")
    )
    private List<Book> books = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
