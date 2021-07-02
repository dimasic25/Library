package org.springframework.boot.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.model.Author;
import org.springframework.boot.library.model.Book;
import org.springframework.boot.library.model.Genre;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BookDaoImpl implements BookDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

      String sql = "INSERT INTO book_user(book_id, user_id) VALUES(?, ?)";
      jdbcTemplate.update(sql, book_id, user_id);

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

