package org.springframework.boot.library.repository;

import org.springframework.boot.library.model.Notification;

import java.time.LocalDate;
import java.util.List;

public interface NotificationRepo {
    List<Notification> findAll();

    void takeBookNote(String message, LocalDate date, String user_name, String book_name);

    void returnBookNote(String message, LocalDate date, String user_name, String book_name);
}
