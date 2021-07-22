package org.springframework.boot.library.listeners;

import org.springframework.boot.library.events.TakeBookEvent;
import org.springframework.boot.library.repository.NotificationRepo;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TakeBookEventListener implements ApplicationListener<TakeBookEvent> {
    private final NotificationRepo notificationRepo;

    public TakeBookEventListener(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    @Override
    public void onApplicationEvent(TakeBookEvent takeBookEvent) {

        notificationRepo.takeBookNote(takeBookEvent.getMessage(), takeBookEvent.getDate(), takeBookEvent.getUser_name(), takeBookEvent.getBook_name());

    }
}
