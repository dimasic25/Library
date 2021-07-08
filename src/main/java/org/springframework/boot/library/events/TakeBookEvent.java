package org.springframework.boot.library.events;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

public class TakeBookEvent extends ApplicationEvent {
    private String message;
    private LocalDate date;
    private String user_name;
    private String book_name;


    public TakeBookEvent(Object source, String message, LocalDate date, String user_name, String book_name) {
        super(source);
        this.message = message;
        this.date = date;
        this.user_name = user_name;
        this.book_name = book_name;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getBook_name() {
        return book_name;
    }
}
