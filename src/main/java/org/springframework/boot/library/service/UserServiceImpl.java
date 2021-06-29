package org.springframework.boot.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.dao.UserDAO;
import org.springframework.boot.library.entity.User;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public int saveUser(User user) {
        try {
            userDAO.save(user);
        } catch (SQLException throwables) {
           return 0;
        }
        return 1;
    }
}
