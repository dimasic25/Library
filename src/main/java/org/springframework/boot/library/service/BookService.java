package org.springframework.boot.library.service;

import org.springframework.boot.library.model.Book;
import org.springframework.boot.library.model.DateBook;
import org.springframework.boot.library.model.User;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    Book getBook(int id);

    void takeBook(int user_id, int book_id);

    void returnBook(int user_id, int book_id);

    List<DateBook> returnBooksForPeriod(String begin, String end);

    void saveBook(Book book);

    void updateBook(int id, Book book);

    void deleteBook(int id);
}
