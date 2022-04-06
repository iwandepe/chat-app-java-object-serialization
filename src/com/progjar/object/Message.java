package com.progjar.object;

import java.io.Serializable;

public class Message implements Serializable {
    private String sender;
    private String text;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
