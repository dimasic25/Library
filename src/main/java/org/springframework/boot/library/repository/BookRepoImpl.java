package org.springframework.boot.library.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.mappers.BookMapper;
import org.springframework.boot.library.mappers.DateBookMapper;
import org.springframework.boot.library.model.*;
import org.springframework.boot.library.publishers.BookEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Repository
public class BookRepoImpl implements BookRepo {
    private final JdbcTemplate jdbcTemplate;
    private final BookEventPublisher eventPublisher;

    @Autowired
    public BookRepoImpl(JdbcTemplate jdbcTemplate, BookEventPublisher eventPublisher) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventPublisher = eventPublisher;
    }


    @Override
    public Book findById(int id) {
        String sql = "SELECT books.id as book_id,\n" +
                "       books.name as book_name,\n" +
                "       authors.id as author_id,\n" +
                "       authors.name as author_name,\n" +
                "       (SELECT array_agg(genres.name) as genres_name\n" +
                "        FROM genres\n" +
                "                 INNER JOIN book_genre on genres.id = book_genre.genre_id AND book_genre.book_id = ?),\n" +
                "       (SELECT array_agg(genres.id) as genres_id\n" +
                "        FROM genres\n" +
                "                 INNER JOIN book_genre on genres.id = book_genre.genre_id AND book_genre.book_id = ?)\n" +
                "FROM books\n" +
                "         INNER JOIN authors\n" +
                "                    ON authors.id = books.author_id\n" +
                "WHERE books.id = ?;";

        return jdbcTemplate.queryForObject(sql, new BookMapper(), id, id, id);
    }

    @Override
    public void takeBook(int user_id, int book_id) {
        String sql_logic = "SELECT book_id FROM book_user WHERE book_id=? AND user_id=? AND date_return IS NULL";
        List<Integer> flag = jdbcTemplate.queryForList(sql_logic, Integer.class, book_id, user_id);

        if (flag.size() == 0) {

            LocalDate date = LocalDate.now();
            Date date_taking = Date.valueOf(date);
            String sql = "INSERT INTO book_user(date_taking, book_id, user_id) VALUES(?, ?, ?)";

            jdbcTemplate.update(sql, date_taking, book_id, user_id);

            String sql2 = "SELECT concat_ws(' ', first_name, last_name) FROM users WHERE id = ?";
            String user_name = jdbcTemplate.queryForObject(sql2, String.class, user_id);

            String book_name = findById(book_id).getName();

            eventPublisher.publishTakeBookEvent("", date, user_name, book_name);
        }
    }

    @Override
    public void returnBook(int user_id, int book_id) {
        LocalDate date = LocalDate.now();
        Date date_return = Date.valueOf(date);

        String sql = "UPDATE book_user SET date_return=? WHERE book_id=? AND user_id=? AND date_return IS NULL";
        jdbcTemplate.update(sql, date_return, book_id, user_id);

        String sql2 = "SELECT concat_ws(' ', first_name, last_name) FROM users WHERE id = ?";
        String user_name = jdbcTemplate.queryForObject(sql2, String.class, user_id);

        String book_name = findById(book_id).getName();

        eventPublisher.publishReturnBookEvent("", date, user_name, book_name);
    }

    @Override
    public List<DateBook> findAllBooksForPeriod(LocalDate begin, LocalDate end) {
        Date date_begin = Date.valueOf(begin);
        Date date_end = Date.valueOf(end);
        String sql = "SELECT date_taking, date_return," +
                " users.first_name, users.last_name," +
                " books.name as book_name " +
                " FROM book_user INNER JOIN users ON users.id = book_user.user_id" +
                " INNER JOIN books ON books.id = book_user.book_id" +
                " WHERE date_taking>=? AND date_return<=?";

        return jdbcTemplate.query(sql, new DateBookMapper(), date_begin, date_end);
    }

    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books(name, author_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, book.getName(), book.getAuthor().getId());

        String sql_book = "SELECT id FROM books WHERE name = ? AND author_id = ?";
        Integer book_id = jdbcTemplate.queryForObject(sql_book, Integer.class, book.getName(), book.getAuthor().getId());

        String sql_genre = "INSERT INTO book_genre(book_id, genre_id) VALUES(?, ?)";

        List<Genre> genres = book.getGenres();

        for (Genre genre :
                genres) {
            jdbcTemplate.update(sql_genre, book_id, genre.getId());
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
    public List<Book> findBooksUser(int user_id) {

        String sql = "SELECT books.id as book_id, books.name as book_name,\n" +
                "       authors.id as author_id, authors.name as author_name,\n" +
                "(SELECT array_agg(genres.name) as genres_name\n" +
                "FROM genres\n" +
                "INNER JOIN book_genre on genres.id = book_genre.genre_id AND book_genre.book_id = books.id),\n" +
                "(SELECT array_agg(genres.id) as genres_id\n" +
                "FROM genres\n" +
                "INNER JOIN book_genre on genres.id = book_genre.genre_id AND book_genre.book_id = books.id)\n" +
                "FROM books INNER JOIN authors ON authors.id = books.author_id\n" +
                "           INNER JOIN book_genre ON book_genre.book_id = books.id\n" +
                "           INNER JOIN genres ON book_genre.genre_id = genres.id\n" +
                "INNER JOIN book_user ON book_user.user_id = ? AND books.id = book_user.book_id AND book_user.date_return IS NULL";

        List<Book> books = jdbcTemplate.query(sql, new BookMapper(), user_id);
        books.removeIf(Objects::isNull);
        return books;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT books.id as book_id,\n" +
                "       books.name as book_name,\n" +
                "       authors.id as author_id,\n" +
                "       authors.name as author_name,\n" +
                "       (SELECT array_agg(genres.name) as genres_name\n" +
                "        FROM genres\n" +
                "                 INNER JOIN book_genre on genres.id = book_genre.genre_id AND book_genre.book_id = books.id),\n" +
                "       (SELECT array_agg(genres.id) as genres_id\n" +
                "        FROM genres\n" +
                "                 INNER JOIN book_genre on genres.id = book_genre.genre_id AND book_genre.book_id = books.id)\n" +
                "FROM books\n" +
                "         INNER JOIN authors\n" +
                "                    ON authors.id = books.author_id\n";

        return jdbcTemplate.query(sql, new BookMapper());
    }


}

