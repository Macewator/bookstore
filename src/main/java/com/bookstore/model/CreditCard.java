package com.bookstore.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "credit_card")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_credit_card")
    private Long id;

    @NotBlank
    @Column(name = "owner_name")
    private String ownerName;

    @Pattern(regexp = "\\d{16}")
    @Column(name = "credit_card_number", unique = true)
    private String creditCardNumber;

    @Pattern(regexp = "\\d{2}-\\d{4}")
    @Column(name = "expire_date")
    private String expireDate;

    @Pattern(regexp = "\\d{3}")
    @Column(name = "cvv_number", unique = true)
    private String cvvNumber;

    @OneToOne(mappedBy = "creditCards")
    private Client client;

    public CreditCard() {
    }

    public CreditCard(@NotBlank String ownerName, @Pattern(regexp = "\\d{16}") String creditCardNumber,
                      @Pattern(regexp = "\\d{2}-\\d{4}") String expireDate, @Pattern(regexp = "\\d{3}") String cvvNumber) {
        this.ownerName = ownerName;
        this.creditCardNumber = creditCardNumber;
        this.expireDate = expireDate;
        this.cvvNumber = cvvNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getCvvNumber() {
        return cvvNumber;
    }

    public void setCvvNumber(String cvvNumber) {
        this.cvvNumber = cvvNumber;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
