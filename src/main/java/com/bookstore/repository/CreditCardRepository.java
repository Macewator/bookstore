package com.bookstore.repository;

import com.bookstore.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    @Query("SELECT c.ownerName FROM CreditCard c WHERE c.ownerName=:ownerName")
    Optional<String> findOwnerName(@Param("ownerName") String ownerName);

    @Query("SELECT c.creditCardNumber FROM CreditCard c WHERE c.creditCardNumber=:creditCardNumber")
    Optional<String> findCreditCardNumber(@Param("creditCardNumber") String creditCardNumber);

    @Query("SELECT c.cvvNumber FROM CreditCard c WHERE c.cvvNumber=:cvvNumber")
    Optional<String> findCvvNumber(@Param("cvvNumber") String cvvNumber);
}
