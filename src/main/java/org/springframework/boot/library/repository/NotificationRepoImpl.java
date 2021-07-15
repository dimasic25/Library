package org.springframework.boot.library.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.model.Notification;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationRepoImpl implements NotificationRepo {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NotificationRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Notification> findAll() {
        String sql = "SELECT * FROM notifications";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Notification.class));
    }
}
