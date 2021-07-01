package org.springframework.boot.library.service;

import org.springframework.boot.library.model.User;

import java.util.List;

public interface UserService {
    void saveUser(User user);

    User getUser(int id);

    List<User> getAllUsers();

    void updateUser(int id, User user);

    void deleteUser(int id);
}
