package com.bookstore.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "author")
public class Author implements Comparable<Author>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_author")
    private Long id;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Column(name = "short_bio", unique = true, length = 100)
    private String shortBio;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private Set<Book> books = new TreeSet<>();

    public Author() {
    }

    public Author(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String shortBio) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.shortBio = shortBio;
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

    @Override
    public int compareTo(Author o) {
        return this.getLastName().compareToIgnoreCase(o.getLastName());
    }
}
