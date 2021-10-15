package com.nguyenloi.chatfirebase;

public class ModelClass {
    String message, from;

    public ModelClass(String message, String from) {
        this.message = message;
        this.from = from;
    }

    public ModelClass() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
