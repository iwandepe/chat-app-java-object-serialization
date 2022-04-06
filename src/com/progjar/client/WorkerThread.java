package com.progjar.client;

import com.progjar.object.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;

public class WorkerThread extends Thread {
    private ObjectInputStream ois;

    public WorkerThread(ObjectInputStream ois) {
        this.ois = ois;
    }

    public void run() {
        while(true) {
            try {
                Message message = (Message) ois.readObject();

                System.out.println(message.getSender() + ": " + message.getText());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
