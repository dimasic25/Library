package org.springframework.boot.library.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class DateBook {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date_taking;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date_return;
    private User user;
    private Book book;

    public DateBook() {
    }

    public DateBook(LocalDate date_taking, LocalDate date_return, User user, Book book) {
        this.date_taking = date_taking;
        this.date_return = date_return;
        this.user = user;
        this.book = book;
    }

    public LocalDate getDate_taking() {
        return date_taking;
    }

    public void setDate_taking(LocalDate date_taking) {
        this.date_taking = date_taking;
    }

    public LocalDate getDate_return() {
        return date_return;
    }

    public void setDate_return(LocalDate date_return) {
        this.date_return = date_return;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
