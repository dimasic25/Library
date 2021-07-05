package org.springframework.boot.library.repository;

import org.springframework.boot.library.model.User;

import java.util.List;

public interface UserRepo {
    void save(User user);

    User findById(int id);

    List<User> findAll();

    void update(int id, User user);

    void delete(int id);
}
