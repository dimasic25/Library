package org.springframework.boot.library.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.model.*;
import org.springframework.boot.library.publishers.BookEventPublisher;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
        String sql = "SELECT id, name FROM books WHERE id = ?";

        Book book = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Book.class), id).stream().findAny().orElseThrow();

        book.setAuthor(getAuthor(id));
        book.setGenres(getGenres(id));

        return book;
    }

    private Author getAuthor(int book_id) {
        String sql = "SELECT authors.id, authors.name FROM authors INNER JOIN books ON authors.id = books.author_id WHERE books.id = ?";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Author.class), book_id).stream().findAny().orElseThrow();
    }

    private List<Genre> getGenres(int book_id) {
        String sql = "SELECT genres.id, genres.name FROM genres\n" +
                "    INNER JOIN book_genre ON book_genre.book_id = ?\n" +
                "                                 AND book_genre.genre_id = genres.id";

        return  jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Genre.class), book_id);
    }

    @Override
    public void takeBook(int user_id, int book_id) {
        String sql_logic = "SELECT id FROM dates WHERE book_id=? AND user_id=? AND date_return IS NULL";
        List<Integer> flag = jdbcTemplate.queryForList(sql_logic, Integer.class, book_id, user_id);

        if (flag.size() == 0) {

            LocalDate date = LocalDate.now();
            Date date_taking = Date.valueOf(date);
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
            String book_name = findById(book_id).getName();

            eventPublisher.publishTakeBookEvent("", date, user_name, book_name);
        }
    }

    @Override
    public void takeBookEvent(String message, LocalDate date, String user_name, String book_name) {

        message = user_name + " взял книгу " + book_name + " " + date;

        String sql = "INSERT INTO notifications(message) VALUES(?)";
        jdbcTemplate.update(sql, message);
    }

    @Override
    public void returnBook(int user_id, int book_id) {
            LocalDate date = LocalDate.now();
            Date date_return = Date.valueOf(date);

            String sql = "UPDATE dates SET date_return=? WHERE book_id=? AND user_id=? AND date_return IS NULL";
            jdbcTemplate.update(sql, date_return, book_id, user_id);

            String sql2 = "DELETE FROM book_user WHERE book_id=? AND user_id=?";
            jdbcTemplate.update(sql2, book_id, user_id);

            String sql3 = "SELECT first_name FROM users WHERE id = ?";
            List<String> list = jdbcTemplate.queryForList(sql3, String.class, user_id);
            String first_name = list.get(0);


            String sql4 = "SELECT last_name FROM users WHERE id = ?";
            list = jdbcTemplate.queryForList(sql4, String.class, user_id);
            String last_name = list.get(0);

            String user_name = first_name + " " + last_name;
            String book_name = findById(book_id).getName();

            eventPublisher.publishReturnBookEvent("", date, user_name, book_name);
    }

    @Override
    public void returnBookEvent(String message, LocalDate date, String user_name, String book_name) {
        message = user_name + " вернул книгу " + book_name + " " + date;

        String sql = "INSERT INTO notifications(message) VALUES(?)";
        jdbcTemplate.update(sql, message);
    }

    @Override
    public List<DateBook> findAllBooksForPeriod(LocalDate begin, LocalDate end) {
        java.sql.Date date_begin = java.sql.Date.valueOf(begin);
        java.sql.Date date_end = java.sql.Date.valueOf(end);
        String sql = "SELECT id, date_taking, date_return FROM dates WHERE date_taking>? AND date_return<?";
        List<DateBook> date_books = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DateBook.class), date_begin, date_end);

        String sql_id = "SELECT id FROM dates WHERE date_taking>? AND date_return<?";
        List<Integer> dates_id = jdbcTemplate.queryForList(sql_id, Integer.class, date_begin, date_end);

        String sql_userId = "SELECT user_id FROM dates WHERE id = ?";
        List<Integer> users_id = new ArrayList<>();
        for (int date_id:
             dates_id) {
            users_id.add((jdbcTemplate.queryForList(sql_userId, Integer.class, date_id)).get(0));
        }

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

        String sql3 = "SELECT * FROM users WHERE" +
                " id = ?";
        List<User> users = new ArrayList<>();
        for (int user_id:
             users_id) {
            users.addAll(jdbcTemplate.query(sql3, new BeanPropertyRowMapper<>(User.class), user_id));
        }

        index = 0;
        for (DateBook el :
                date_books) {
            User user = users.get(index);
            el.setUser(user);
            index++;
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
    public List<Book> findBooksUser(int user_id) {
        String sql = "SELECT book_id FROM book_user WHERE user_id=?";
        List<Integer> books_id = jdbcTemplate.queryForList(sql, Integer.class, user_id);

        List<Book> books = new ArrayList<>();
        for (int id :
                books_id) {
            books.add(findById(id));
        }

        return books;
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

