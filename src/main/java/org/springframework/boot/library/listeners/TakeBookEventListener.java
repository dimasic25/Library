package org.springframework.boot.library.listeners;

import org.springframework.boot.library.events.ReturnBookEvent;
import org.springframework.boot.library.events.TakeBookEvent;
import org.springframework.boot.library.repository.BookRepo;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TakeBookEventListener implements ApplicationListener<TakeBookEvent> {
    private final BookRepo bookRepo;

    public TakeBookEventListener(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public void onApplicationEvent(TakeBookEvent takeBookEvent) {

        bookRepo.takeBookEvent(takeBookEvent.getMessage(), takeBookEvent.getDate(), takeBookEvent.getUser_name(), takeBookEvent.getBook_name());

    }
}
