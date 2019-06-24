package com.bookstore.model;

import com.bookstore.validate.Zip;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
public class Address {

    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @Zip
    private String zip;


    public Address() {
    }

    public Address(@NotBlank String street, @NotBlank String city, @Zip String zip) {
        this.street = street;
        this.city = city;
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
