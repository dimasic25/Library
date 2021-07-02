package org.springframework.boot.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.dao.BookDao;
import org.springframework.boot.library.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;

    @Autowired
    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.findAll();
    }

    @Override
    public Book getBook(int id) {
        try {
            return bookDao.findById(id);
        } catch (NoSuchElementException exception) {
            return null;
        }
    }

    @Override
    public void takeBook(int user_id, int book_id) {
        bookDao.takeBook(user_id, book_id);
    }

    @Override
    public void returnBook(int user_id, int book_id) {
        bookDao.returnBook(user_id, book_id);
    }
}
