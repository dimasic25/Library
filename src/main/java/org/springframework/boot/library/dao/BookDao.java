package org.springframework.boot.library.dao;


import org.springframework.boot.library.model.Book;

import java.util.List;

public interface BookDao {
    List<Book> findAll();

    Book findById(int id);

    void takeBook(int user_id, int book_id);
}
