package org.springframework.boot.library.service;

import org.springframework.boot.library.model.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getAllNotifications();
}
