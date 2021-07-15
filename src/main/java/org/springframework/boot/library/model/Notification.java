package org.springframework.boot.library.model;

public class Notification {
    private int id;
    private String message;

    public Notification() {

    }

    public Notification(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
