package org.springframework.boot.library.repository;


import org.springframework.boot.library.model.Book;
import org.springframework.boot.library.model.DateBook;

import java.time.LocalDate;
import java.util.List;

public interface BookRepo {
    List<Book> findAll();

    Book findById(int id);

    void takeBook(int user_id, int book_id);

    void takeBookEvent(String message, LocalDate date, String user_name, String book_name);

    void returnBook(int user_id, int book_id);

    void returnBookEvent(String message, LocalDate date, String user_name, String book_name);

    List<DateBook> findAllBooksForPeriod(LocalDate begin, LocalDate end);

    void save(Book book);

    void update(int id, Book book);

    void delete(int id);

    List<Book> findBooksUser(int id);
}
