package com.bookstore.service;

import com.bookstore.model.CreditCard;
import com.bookstore.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreditCardService {

    private CreditCardRepository creditCardRepository;

    @Autowired
    public CreditCardService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    public CreditCard checkRegisterPayment(CreditCard creditCard){
        StringBuilder message = new StringBuilder();
        message.append("uwaga:");
        Optional<String> ownerName = creditCardRepository.findOwnerName(creditCard.getOwnerName());
        Optional<String> creditCardNumber = creditCardRepository.findCreditCardNumber(creditCard.getCreditCardNumber());
        Optional<String> cvvNumber = creditCardRepository.findCvvNumber(creditCard.getCvvNumber());
        if (ownerName.isPresent()) {
            message.append(" podany właściciel jest już zarejestrowany,");
        }
        if (creditCardNumber.isPresent()) {
            message.append(" podany numer karty jest już w bazie,");
        }
        if (cvvNumber.isPresent()) {
            message.append(" podany numer cvv jest już w bazie");
        }
        if (!message.toString().equals("uwaga:")) {
            throw new IllegalArgumentException(message.toString());
        }
        return creditCard;
    }
}
