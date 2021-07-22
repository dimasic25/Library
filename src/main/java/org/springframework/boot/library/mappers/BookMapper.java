package org.springframework.boot.library.mappers;


import org.springframework.boot.library.model.Author;
import org.springframework.boot.library.model.Book;
import org.springframework.boot.library.model.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getInt("book_id"));
        book.setName(resultSet.getString("book_name"));

        Author author = new Author();
        author.setId(resultSet.getInt("author_id"));
        author.setName(resultSet.getString("author_name"));

        List<Genre> genres = new ArrayList<>();
        do { Genre genre = new Genre();
        genre.setId(resultSet.getInt("genre_id"));
        genre.setName(resultSet.getString("genre_name"));
        genres.add(genre);
        }
        while (resultSet.next());

        book.setAuthor(author);
        book.setGenres(genres);

        return book;
    }
}
