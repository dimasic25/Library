package org.springframework.boot.library.dao;

import org.springframework.boot.library.entity.User;

import java.sql.SQLException;

public interface UserDAO {
    void save(User user) throws SQLException;
}
