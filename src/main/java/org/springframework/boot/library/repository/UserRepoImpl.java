package org.springframework.boot.library.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepoImpl implements UserRepo {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users(first_name, last_name, email) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, user.getFirst_name(), user.getLast_name(), user.getEmail());
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

    @Override
    public void update(int id, User user) {
        String sql = "UPDATE users SET first_name=?, last_name=?, email=? WHERE id=?";
        jdbcTemplate.update(sql, user.getFirst_name(), user.getLast_name(), user.getEmail(), id);
    }

    @Override
    public void delete(int id) {
        String sql_dates = "DELETE FROM dates WHERE user_id=?";
        jdbcTemplate.update(sql_dates, id);

        String sql = "DELETE FROM users WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

}
