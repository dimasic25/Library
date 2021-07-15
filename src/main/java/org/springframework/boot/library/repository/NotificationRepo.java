package org.springframework.boot.library.repository;

import org.springframework.boot.library.model.Notification;

import java.util.List;

public interface NotificationRepo {
    List<Notification> findAll();
}
