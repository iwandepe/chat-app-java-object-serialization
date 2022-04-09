package com.progjar.object;

import java.io.Serializable;

public class Message implements Serializable {
    private String sender;
    private String text;
    private String receiver;

    public Message() {
        this.receiver = "all";
    }

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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void print() {
        System.out.println("message from " + this.sender + " to " + this.receiver + ": " + this.text);
    }
}
