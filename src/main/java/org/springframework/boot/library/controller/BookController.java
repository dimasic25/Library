package org.springframework.boot.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.model.Book;
import org.springframework.boot.library.model.User;
import org.springframework.boot.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBook(@PathVariable int id) {
        Book book = bookService.getBook(id);
        if (book == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("users/{user_id}/books/{book_id}")
    public ResponseEntity<Integer> takeBook(@PathVariable int user_id, @PathVariable int book_id) {
        bookService.takeBook(user_id, book_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("users/{user_id}/books/{book_id}")
    public ResponseEntity<Integer> returnBook(@PathVariable int user_id, @PathVariable int book_id) {
        bookService.returnBook(user_id, book_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
