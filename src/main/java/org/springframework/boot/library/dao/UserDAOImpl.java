package org.springframework.boot.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
public class UserDAOImpl implements UserDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users(first_name, last_name, email) VALUES(?, ?, ?)";
        if (jdbcTemplate.update(sql, user.getFirst_name(), user.getLast_name(), user.getEmail()) != 1) {
            throw new SQLException("INSERT Error!");
        }
    }
}
