package com.bookstore.repository;

import com.bookstore.model.Address;
import com.bookstore.model.Client;
import com.bookstore.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUserName(String clientName);

    @Query("SELECT c.userName FROM Client c WHERE c.userName=:username")
    Optional<String> findUserName(@Param("username") String userName);

    @Query("SELECT c.userInfo.telephone FROM Client c WHERE c.userInfo.telephone=:telephone")
    Optional<String> findTelephone(@Param("telephone") String telephone);

    @Query("SELECT c.userInfo.email FROM Client c WHERE c.userInfo.email=:email")
    Optional<String> findEmail(@Param("email") String email);

    @Query("SELECT c.address FROM Client c WHERE c.userName=:username")
    Address findClientAddress(@Param("username") String userName);

    @Transactional
    void deleteByUserName(String userName);
}
