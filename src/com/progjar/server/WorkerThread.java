package com.progjar.server;

import com.progjar.object.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkerThread extends Thread {
    private Socket socket;
    private ObjectOutputStream ous;
    private ObjectInputStream ois;
    private ServerThread serverThread;

    public WorkerThread(Socket socket, ServerThread serverThread) {
        try {
            this.socket = socket;
            this.ous = new ObjectOutputStream(this.socket.getOutputStream());
            this.ois = new ObjectInputStream(this.socket.getInputStream());
            this.serverThread = serverThread;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                Message message = (Message) this.ois.readObject();

                this.serverThread.sendToAll(message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(Message message) {
        try {
            this.ous.writeObject(message);
            this.ous.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
