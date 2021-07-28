package org.springframework.boot.library.mappers;


import org.springframework.boot.library.model.Author;
import org.springframework.boot.library.model.Book;
import org.springframework.boot.library.model.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            Book book = new Book();

            book.setId( resultSet.getInt("book_id"));
            book.setName(resultSet.getString("book_name"));

            Author author = new Author();
            author.setId(resultSet.getInt("author_id"));
            author.setName(resultSet.getString("author_name"));

            List<Genre> genres = new ArrayList<>();

            Array id = resultSet.getArray("genres_id");
            Integer[] genres_id = (Integer[]) id.getArray();
            Array names = resultSet.getArray("genres_name");
            String[] genres_name = (String[]) names.getArray();

            for (int count = 0; count < genres_id.length; count++) {
                Genre genre = new Genre();
                genre.setId(genres_id[count]);
                genre.setName(genres_name[count]);
                genres.add(genre);
            }

            book.setAuthor(author);
            book.setGenres(genres);
            return book;
    }
}
