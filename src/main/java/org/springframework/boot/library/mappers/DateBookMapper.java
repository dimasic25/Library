package org.springframework.boot.library.mappers;


import org.springframework.boot.library.model.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DateBookMapper implements RowMapper<DateBook> {
    @Override
    public DateBook mapRow(ResultSet resultSet, int i) throws SQLException {
        DateBook dateBook = new DateBook();
        dateBook.setDate_return(resultSet.getDate("date_return").toLocalDate());
        dateBook.setDate_taking(resultSet.getDate("date_taking").toLocalDate());

        User user = new User();

        user.setFirst_name(resultSet.getString("first_name"));
        user.setLast_name(resultSet.getString("last_name"));

        Book book = new Book();

        book.setName(resultSet.getString("book_name"));

        dateBook.setUser(user);
        dateBook.setBook(book);

        return dateBook;
    }
}
