package org.springframework.boot.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.model.Notification;
import org.springframework.boot.library.repository.NotificationRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepo notificationRepo;

    @Autowired
    public NotificationServiceImpl(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepo.findAll();
    }
}
