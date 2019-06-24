package com.bookstore.dto;

import com.bookstore.model.Author;
import com.bookstore.model.Publisher;

public class BookDto {

    private Long isbn;
    private String title;
    private String description;
    private Double price;
    private String status;
    private Author author;
    private Publisher publisher;

    public BookDto() {
    }

    public BookDto(Long isbn, String title, String description, Double price, String status, Author author, Publisher publisher) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.price = price;
        this.status = status;
        this.author = author;
        this.publisher = publisher;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
