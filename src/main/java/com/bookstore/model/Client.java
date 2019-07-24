package com.bookstore.model;

import com.bookstore.validate.Password;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 5206902368022108208L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Long id;

    @NotBlank
    @Column(name = "user_name", unique = true)
    private String userName;

    @Password
    private String password;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @Valid
    @Embedded
    private Information userInfo;

    @Valid
    @Embedded
    private Address address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "client_roles",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "client_role_id"))
    private Set<ClientRole> clientRoles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCards;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Order> orders;

    public Client() {
    }

    public Client(@NotBlank String userName, @Password String password, @NotBlank String firstName,
                  @NotBlank String lastName, Information userInfo, Address address) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userInfo = userInfo;
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Information getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Information userInfo) {
        this.userInfo = userInfo;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<ClientRole> getClientRoles() {
        return clientRoles;
    }

    public void setClientRoles(Set<ClientRole> clientRoles) {
        this.clientRoles = clientRoles;
    }

    public CreditCard getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(CreditCard creditCards) {
        this.creditCards = creditCards;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}
