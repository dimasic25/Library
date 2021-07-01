package org.springframework.boot.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.dao.UserDAO;
import org.springframework.boot.library.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void saveUser(User user) {
        userDAO.save(user);
    }

    @Override
    public User getUser(int id) {
        try {
            return userDAO.findById(id);
        } catch (NoSuchElementException exception) {
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public void updateUser(int id, User user) {
        userDAO.update(id, user);
    }

    @Override
    public void deleteUser(int id) {
        userDAO.delete(id);
    }
}
