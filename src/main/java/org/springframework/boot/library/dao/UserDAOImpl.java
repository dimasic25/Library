package org.springframework.boot.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

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

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id=?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), id)
                .stream().findAny().orElseThrow();
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }
}
