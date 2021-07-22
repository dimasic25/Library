package org.springframework.boot.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.model.Book;
import org.springframework.boot.library.model.DateBook;
import org.springframework.boot.library.model.User;
import org.springframework.boot.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{book_id}")
    public ResponseEntity<Book> getBook(@PathVariable int book_id) {
        Book book = bookService.getBook(book_id);
        if (book == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PutMapping("/{book_id}/take")
    public ResponseEntity<Integer> takeBook(@RequestParam int user_id, @PathVariable int book_id) {
        bookService.takeBook(user_id, book_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{book_id}/return")
    public ResponseEntity<Integer> returnBook(@RequestParam int user_id, @PathVariable int book_id) {
        bookService.returnBook(user_id, book_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/books-period")
    public ResponseEntity<List<DateBook>> returnBooksForPeriod(@RequestParam(required = false) String begin,
                                                               @RequestParam(required = false) String end) {
        List<DateBook> books = bookService.returnBooksForPeriod(begin, end);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        bookService.saveBook(book);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{book_id}")
    public ResponseEntity<User> updateBook(@PathVariable int book_id, @RequestBody Book book) {
        bookService.updateBook(book_id, book);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/books-user")
    public ResponseEntity<List<Book>> getBooksUser(@RequestParam int user_id) {
        List<Book> books = bookService.getBooksUser(user_id);

        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
