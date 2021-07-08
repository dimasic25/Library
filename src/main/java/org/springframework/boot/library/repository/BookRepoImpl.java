package org.springframework.boot.library.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.model.*;
import org.springframework.boot.library.publishers.TakeBookEventPublisher;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Repository
public class BookRepoImpl implements BookRepo {
    private final JdbcTemplate jdbcTemplate;
    private final TakeBookEventPublisher eventPublisher;

    @Autowired
    public BookRepoImpl(JdbcTemplate jdbcTemplate, TakeBookEventPublisher eventPublisher) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventPublisher = eventPublisher;
    }


    @Override
    public Book findById(int id) {
        String sql = "SELECT id, name FROM books WHERE id=?";

        Book book = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Book.class), id).stream().findAny().orElseThrow();

        // запрашиваем автора книги
        String sql2 = "SELECT authors.id, authors.name FROM authors\n" +
                "INNER JOIN books on authors.id = books.author_id\n" +
                "WHERE books.id=?";

        Author author = jdbcTemplate.query(sql2, new BeanPropertyRowMapper<>(Author.class), id).stream().findAny().orElseThrow();


        // запрашиваем жанры книги
        String sql3 = "SELECT genres.id, genres.name\n" +
                "FROM genres\n" +
                "         INNER JOIN book_genre on genres.id = book_genre.genre_id\n" +
                "         INNER JOIN books on book_genre.book_id = books.id\n" +
                "WHERE books.id=?";

        List<Genre> genres = jdbcTemplate.query(sql3, new BeanPropertyRowMapper<>(Genre.class), id);

        book.setAuthor(author);
        book.setGenres(genres);
        return book;
    }

    @Override
    public void takeBook(int user_id, int book_id) {

        Date date = new Date();
        Timestamp date_taking = new Timestamp(date.getTime());
        String sql = "INSERT INTO dates(date_taking, book_id, user_id) VALUES(?, ?, ?)";

        jdbcTemplate.update(sql, date_taking, book_id, user_id);

        String sql2 = "INSERT INTO book_user(book_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(sql2, book_id, user_id);

        String sql3 = "SELECT first_name FROM users WHERE id = ?";
        List<String> list = jdbcTemplate.queryForList(sql3, String.class, user_id);
        String first_name = list.get(0);


        String sql4 = "SELECT last_name FROM users WHERE id = ?";
        list = jdbcTemplate.queryForList(sql4, String.class, user_id);
        String last_name = list.get(0);

        String user_name = first_name + " " + last_name;
        LocalDate localDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
        String book_name = findById(book_id).getName();

        eventPublisher.publishTakeBookEvent("", localDate, user_name, book_name);
    }

    @Override
    public void takeBookEvent(String message, LocalDate date, String user_name, String book_name) {

        message = user_name + " взял книгу " + book_name + " " + date;

        String sql = "INSERT INTO notifications(message) VALUES(?)";
        jdbcTemplate.update(sql, message);
    }

    @Override
    public void returnBook(int user_id, int book_id) {
        Date date = new Date();
        Timestamp date_return = new Timestamp(date.getTime());

        String sql = "UPDATE dates SET date_return=? WHERE book_id=? AND user_id=?";
        jdbcTemplate.update(sql, date_return, book_id, user_id);

        String sql2 = "DELETE FROM book_user WHERE book_id=? AND user_id=?";
        jdbcTemplate.update(sql2, book_id, user_id);
    }

    @Override
    public List<DateBook> findAllBooksForPeriod(LocalDate begin, LocalDate end) {
        java.sql.Date date_begin = java.sql.Date.valueOf(begin);
        java.sql.Date date_end = java.sql.Date.valueOf(end);
        String sql = "SELECT id, date_taking, date_return FROM dates WHERE date_taking>? AND date_return<?";
        List<DateBook> date_books = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DateBook.class), date_begin, date_end);

        String sql2 = "SELECT book_id FROM dates WHERE date_taking>? AND date_return<?";
        List<Integer> books_id = jdbcTemplate.queryForList(sql2, Integer.class, date_begin, date_end);

        int index = 0;
        for (int book_id :
                books_id) {
            Book book = findById(book_id);
            DateBook dateBook = date_books.get(index);
            dateBook.setBook(book);
            index++;
        }

        String sql3 = "SELECT users.id, users.first_name, users.last_name, users.email FROM users" +
                " INNER JOIN dates ON dates.user_id = users.id AND dates.date_taking>? AND dates.date_return<?";
        List<User> users = jdbcTemplate.query(sql3, new BeanPropertyRowMapper<>(User.class), date_begin, date_end);

        index = 0;
        for (DateBook el :
                date_books) {
            User user = users.get(index);
            el.setUser(user);
        }

        return date_books;
    }

    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books(name, author_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, book.getName(), book.getAuthor().getId());

        String sql_book = "SELECT * FROM books WHERE name = ? AND author_id = ?";
        Book newBook = jdbcTemplate.query(sql_book, new BeanPropertyRowMapper<>(Book.class), book.getName(), book.getAuthor().getId())
                .stream().findAny().orElseThrow();

        String sql_genre = "INSERT INTO book_genre(book_id, genre_id) VALUES(?, ?)";

        List<Genre> genres = book.getGenres();


        for (Genre genre :
                genres) {
            jdbcTemplate.update(sql_genre, newBook.getId(), genre.getId());
        }

    }

    @Override
    public void update(int id, Book book) {
        String sql = "UPDATE books SET name =?, author_id=? WHERE id=?";
        jdbcTemplate.update(sql, book.getName(), book.getAuthor().getId(), id);

        String delete_sql = "DELETE FROM book_genre WHERE book_id=?";

        jdbcTemplate.update(delete_sql, id);

        String sql_genre = "INSERT INTO book_genre(book_id, genre_id) VALUES(?, ?)";

        List<Genre> genres = book.getGenres();

        for (Genre genre :
                genres) {
            jdbcTemplate.update(sql_genre, id, genre.getId());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM book_genre WHERE book_id=?";
        jdbcTemplate.update(sql, id);

        String sql1 = "DELETE FROM books WHERE id=?";
        jdbcTemplate.update(sql1, id);
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT id FROM books";
        List<Integer> all_id = jdbcTemplate.queryForList(sql, Integer.class);

        List<Book> books = new ArrayList<>();
        for (int id :
                all_id) {
            books.add(findById(id));
        }

        return books;
    }


}

