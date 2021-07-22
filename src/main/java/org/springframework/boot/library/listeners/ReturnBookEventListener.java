package org.springframework.boot.library.listeners;

import org.springframework.boot.library.events.ReturnBookEvent;
import org.springframework.boot.library.repository.NotificationRepo;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ReturnBookEventListener implements ApplicationListener<ReturnBookEvent> {
    private final NotificationRepo notificationRepo;

    public ReturnBookEventListener(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    @Override
    public void onApplicationEvent(ReturnBookEvent returnBookEvent) {

        notificationRepo.returnBookNote(returnBookEvent.getMessage(), returnBookEvent.getDate(), returnBookEvent.getUser_name(), returnBookEvent.getBook_name());

    }
}
