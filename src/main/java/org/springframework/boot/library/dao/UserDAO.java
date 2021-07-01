package org.springframework.boot.library.dao;

import org.springframework.boot.library.model.User;

import java.util.List;

public interface UserDAO {
    void save(User user);

    User findById(int id);

    List<User> findAll();

    void update(int id, User user);

    void delete(int id);
}
