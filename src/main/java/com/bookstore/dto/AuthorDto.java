package com.bookstore.dto;

import com.bookstore.model.Book;

import java.util.Set;

public class AuthorDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String shortBio;

    private Set<Book> books;

    public AuthorDto() {
    }

    public AuthorDto(Long id, String firstName, String lastName, String shortBio, Set<Book> books) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.shortBio = shortBio;
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
