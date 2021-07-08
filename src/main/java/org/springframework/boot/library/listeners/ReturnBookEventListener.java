package org.springframework.boot.library.listeners;

import org.springframework.boot.library.events.ReturnBookEvent;
import org.springframework.boot.library.repository.BookRepo;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ReturnBookEventListener implements ApplicationListener<ReturnBookEvent> {
    private final BookRepo bookRepo;

    public ReturnBookEventListener(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public void onApplicationEvent(ReturnBookEvent returnBookEvent) {

        bookRepo.returnBookEvent(returnBookEvent.getMessage(), returnBookEvent.getDate(), returnBookEvent.getUser_name(), returnBookEvent.getBook_name());

    }
}
