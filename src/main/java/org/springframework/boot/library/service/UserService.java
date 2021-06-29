package org.springframework.boot.library.service;

import org.springframework.boot.library.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    void saveUser(User user) throws SQLException;

    User findById(int id);

    List<User> findAll();
}
