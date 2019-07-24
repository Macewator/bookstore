package com.bookstore.repository;

import com.bookstore.model.Order;
import com.bookstore.util.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findAllByClient_UserName(String userName);
    List<Order> findAllByStatus(OrderStatus status);
}
