package org.springframework.boot.library.mappers;


import org.springframework.boot.library.model.DateBook;
import org.springframework.boot.library.model.User;
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

        user.setId(resultSet.getInt("user_id"));
        user.setFirst_name(resultSet.getString("first_name"));
        user.setLast_name(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));

        dateBook.setUser(user);

        return dateBook;
    }
}
