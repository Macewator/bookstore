package com.bookstore.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "book")
public class Book implements Comparable<Book>{

    @Id
    @Column(name = "id_book", unique = true, length = 10)
    private Long isbn;

    @NotBlank
    @Column(unique = true)
    private String title;

    @NotBlank
    @Column(unique = true, length = 100)
    private String description;

    @Pattern(regexp = "(\\d+\\.\\d{1,2})", message = "Price format: 00.00")
    private Double price;

    @NotBlank
    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id_author")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "publisher_id", referencedColumnName = "id_publisher")
    private Publisher publisher;

    public Book() {
    }

    public Book(Long isbn, @NotBlank String title, @NotBlank String description, @Pattern(regexp = "(\\d+\\.\\d{1,2})", message = "Price format: 00.00") Double price, @NotBlank String status) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.price = price;
        this.status = status;
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

    @Override
    public int compareTo(Book o) {
        return this.getTitle().compareToIgnoreCase(o.getTitle());
    }
}
