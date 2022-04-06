package com.progjar.server;

import com.progjar.object.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class ServerThread extends Thread {
    private Hashtable<String, WorkerThread> clientList;
    private ServerSocket server;

    public ServerThread() {
        try {
            this.clientList = new Hashtable<String, WorkerThread>();
            this.server = new ServerSocket(9000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        // listen for a new connection
        while(true) {
            try {
                // accept a new connection
                Socket socket = this.server.accept();

                // create a new WorkerThread
                WorkerThread wt = new WorkerThread(socket, this);

                // start the new thread
                wt.start();

                // store the new thread to the hash table
                String clientId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();

                this.clientList.put(clientId, wt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendToAll(Message message) {
        // iterate through all clients
        Enumeration<String> clientKeys = this.clientList.keys();
        while (clientKeys.hasMoreElements()) {
            String clientId = clientKeys.nextElement();

            WorkerThread wt = this.clientList.get(clientId);

            // send the message
            wt.send(message);
        }

    }
}
