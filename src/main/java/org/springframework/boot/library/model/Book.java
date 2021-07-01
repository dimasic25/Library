package org.springframework.boot.library.model;

import java.util.List;

public class Book {
    private int id;
    private String name;
    private Author author;
    private List<Genre> genres;

    public Book() {

    }

    public Book(String name, Author author, List<Genre> genres) {
        this.name = name;
        this.author = author;
        this.genres = genres;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
