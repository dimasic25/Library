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
}
