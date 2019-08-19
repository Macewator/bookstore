package com.bookstore.model;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

@Entity
public class Publisher implements Comparable<Publisher>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publisher")
    private Long id;

    @Column(unique = true)
    private String publisherName;

    @Column(unique = true)
    private String wwwPage;

    @Embedded
    @Column(unique = true)
    private Information publisherInfo;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "publisher", fetch = FetchType.EAGER)
    private Set<Book> books = new TreeSet<>();

    public Publisher() {
    }

    public Publisher(String publisherName, String wwwPage, Information publisherInfo, Address address) {
        this.publisherName = publisherName;
        this.wwwPage = wwwPage;
        this.publisherInfo = publisherInfo;
        this.address = address;
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

    @Override
    public int compareTo(Publisher o) {
        return this.getPublisherName().compareToIgnoreCase(o.getPublisherName());
    }
}
