package com.bookstore.model;

import com.bookstore.validate.TelephoneNumber;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;

@Embeddable
public class Information {

    @Email
    private String email;

    @TelephoneNumber
    private String telephone;

    public Information() {
    }

    public Information(@Email String email, @TelephoneNumber String telephone) {
        this.email = email;
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
