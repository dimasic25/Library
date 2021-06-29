package org.springframework.boot.library.dao;

import org.springframework.boot.library.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    void save(User user) throws SQLException;
    User findById(int id);
    List<User> findAll();
}
