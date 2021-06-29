package org.springframework.boot.library.service;

import org.springframework.boot.library.dao.UserDAOImpl;
import org.springframework.boot.library.entity.User;

public interface UserService {
  int saveUser(User user);
}
