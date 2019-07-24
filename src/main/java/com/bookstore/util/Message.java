package com.bookstore.util;

public class Message {

    private String tittle;
    private String content;

    public Message() {
    }

    public Message(String tittle, String conntent) {
        this.tittle = tittle;
        this.content = conntent;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String conntent) {
        this.content = conntent;
    }
}
