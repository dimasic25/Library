package org.springframework.boot.library.service;

import org.springframework.boot.library.model.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    Book getBook(int id);

    void takeBook(int user_id, int book_id);
}