package org.springframework.boot.library.publishers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.library.events.TakeBookEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TakeBookEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishTakeBookEvent(String message, final LocalDate date, final String user_name, final String book_name) {
        TakeBookEvent takeBookEvent = new TakeBookEvent(this, message, date, user_name, book_name);
        applicationEventPublisher.publishEvent(takeBookEvent);
    }
}
