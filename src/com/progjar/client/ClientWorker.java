package com.progjar.client;

import com.progjar.helper.Helper;
import com.progjar.object.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientWorker extends Thread {
    private ObjectInputStream ois;
    ArrayList<Message> messages;
    private Socket workerSocket;

    public ClientWorker(ObjectInputStream ois, Socket workerSocket) {
        this.ois = ois;
        messages = new ArrayList<Message>();
        this.workerSocket = workerSocket;
    }

    public void run() {
        while(!workerSocket.isClosed()) {
            try {
                Message message = (Message) ois.readObject();
                messages.add(message);

//                Helper.clearConsole();
                System.out.print("\033[H\033[2J");
                System.out.flush();

//                Print all message
//                for (Message msg : this.messages) {
//                    msg.print();
//                }

                System.out.println();
                System.out.print("(NEW) ");
                message.print();
                System.out.println();

                System.out.println("type command:");
            } catch (IOException e) {
                System.out.println(e.getMessage());
//                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
//                e.printStackTrace();
            }

        }
    }
}
