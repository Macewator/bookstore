package com.bookstore.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public class ClientUpdateData {

    @NotBlank
    private String userName;

    @Valid
    private Information userInfo;

    @Valid
    private Address address;

    public ClientUpdateData() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
