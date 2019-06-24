package com.bookstore.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.*;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @NotBlank
    @Column(name = "id_user_name", unique = true, length = 20)
    private String userName;

    @NotBlank
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
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "client_roles",
            joinColumns = @JoinColumn(name = "user_name_id", referencedColumnName = "id_user_name"),
            inverseJoinColumns = @JoinColumn(name = "client_role_id", referencedColumnName = "id_client_role"))
    private Set<ClientRole> clientRoles = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    public Client() {
    }

    public Client(@NotBlank String userName, @NotBlank String password, @NotBlank String firstName, @NotBlank String lastName, Information userInfo, Address address) {
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(userName, client.userName) &&
                Objects.equals(password, client.password) &&
                Objects.equals(firstName, client.firstName) &&
                Objects.equals(lastName, client.lastName) &&
                Objects.equals(userInfo, client.userInfo) &&
                Objects.equals(address, client.address) &&
                Objects.equals(clientRoles, client.clientRoles) &&
                Objects.equals(orders, client.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password, firstName, lastName, userInfo, address, clientRoles, orders);
    }
}
