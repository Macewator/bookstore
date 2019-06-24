package com.bookstore.dto;

import com.bookstore.model.Address;
import com.bookstore.model.Book;
import com.bookstore.model.Information;

import java.util.Set;

public class PublisherDto {

    private Long id;

    private String publisherName;

    private String wwwPage;

    private Information publisherInfo;

    private Address address;

    private Set<Book> books;

    public PublisherDto() {
    }

    public PublisherDto(Long id, String publisherName, String wwwPage, Information publisherInfo, Address address, Set<Book> books) {
        this.id = id;
        this.publisherName = publisherName;
        this.wwwPage = wwwPage;
        this.publisherInfo = publisherInfo;
        this.address = address;
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getWwwPage() {
        return wwwPage;
    }

    public void setWwwPage(String wwwPage) {
        this.wwwPage = wwwPage;
    }

    public Information getPublisherInfo() {
        return publisherInfo;
    }

    public void setPublisherInfo(Information publisherInfo) {
        this.publisherInfo = publisherInfo;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
