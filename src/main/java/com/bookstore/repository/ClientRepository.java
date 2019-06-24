package com.bookstore.repository;

import com.bookstore.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

   Optional<Client> findByUserName(String userName);
}
