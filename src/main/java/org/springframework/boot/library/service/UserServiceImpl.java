package org.springframework.boot.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.repository.UserRepo;
import org.springframework.boot.library.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void saveUser(User user) {
        userRepo.save(user);
    }

    @Override
    public User getUser(int id) {
        try {
            return userRepo.findById(id);
        } catch (NoSuchElementException exception) {
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public void updateUser(int id, User user) {
        userRepo.update(id, user);
    }

    @Override
    public void deleteUser(int id) {
        userRepo.delete(id);
    }
}
