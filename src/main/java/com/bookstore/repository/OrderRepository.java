package com.bookstore.repository;

import com.bookstore.model.Order;
import com.bookstore.util.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByClient_UserName(String userName);

    List<Order> findAllByOrderStatus(OrderStatus status);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM orders WHERE order_status=:orderstatus AND client_user_name=:username")
    void deleteAllByOrderStatusAndClientUserName(@Param("orderstatus")String status, @Param("username") String userName);
}
