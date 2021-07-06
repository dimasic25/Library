package org.springframework.boot.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.repository.BookRepo;
import org.springframework.boot.library.model.Book;
import org.springframework.boot.library.model.DateBook;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepo bookRepo;

    @Autowired
    public BookServiceImpl(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    @Override
    public Book getBook(int id) {
        try {
            return bookRepo.findById(id);
        } catch (NoSuchElementException exception) {
            return null;
        }
    }

    @Override
    public void takeBook(int user_id, int book_id) {
        bookRepo.takeBook(user_id, book_id);
    }

    @Override
    public void returnBook(int user_id, int book_id) {
        bookRepo.returnBook(user_id, book_id);
    }

    @Override
    public List<DateBook> returnBooksForPeriod(String begin, String end) {
        LocalDate from = LocalDate.parse(begin);
        LocalDate to = LocalDate.parse(end);
        return bookRepo.findAllBooksForPeriod(from, to);
    }

    @Override
    public void saveBook(Book book) {
        bookRepo.save(book);
    }

    @Override
    public void updateBook(int id, Book book) {

    }

    @Override
    public void deleteBook(int id) {

    }
}
