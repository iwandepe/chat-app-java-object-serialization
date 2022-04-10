package com.progjar.server;

import com.progjar.object.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Enumeration;

public class ServerWorker extends Thread {
    private Socket socket;
    private ObjectOutputStream ous;
    private ObjectInputStream ois;
    private ServerThread serverThread;
    private Boolean exit;
    private String clientId;

    public ServerWorker(Socket socket, ServerThread serverThread, String clientId) {
        try {
            this.socket = socket;
            this.ous = new ObjectOutputStream(this.socket.getOutputStream());
            this.ois = new ObjectInputStream(this.socket.getInputStream());
            this.serverThread = serverThread;
            this.exit = false;
            this.clientId = clientId;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(!exit) {
            try {
                Message message = (Message) this.ois.readObject();


                System.out.println( message.getText() );
                if (message.getReceiver().equals("system")) {
                    if (message.getText().equals("login")) {
                        String clientId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                        serverThread.clientUserList.put(message.getSender(), clientId);

                        System.out.println( getCurrentActiveUser() );
                    }
                    else if (message.getText().startsWith("close") || message.getText().isEmpty()) {
                        System.out.println( "masuk close" );
                        exit = true;
                        String removedClientUser = serverThread.clientUserList.remove(message.getSender());
                        ServerWorker removedClient = serverThread.clientList.remove(clientId);
                        System.out.println( removedClientUser );
                        System.out.println( removedClient );
                        break;
//                        currentThread().interrupt();
                    }
                }
                else if (message.getReceiver().equals("all")) {
                    this.serverThread.sendToAll(message);
                }
                else if (message.getText().equals("<active>")) {
                    message.setText( getCurrentActiveUser() );
                    this.serverThread.sendToUser(message);
                }
                else {
                    this.serverThread.sendToUser(message);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
//                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
//                e.printStackTrace();
            }
        }
    }

    public String getCurrentActiveUser() {
        StringBuilder activeUser = new StringBuilder("\nactive users:\n");
        Enumeration<String> clientKeys = this.serverThread.clientUserList.keys();
        while (clientKeys.hasMoreElements()) {
            String username = clientKeys.nextElement();
            String client = this.serverThread.clientUserList.get(username);
            activeUser.append(username).append(": ").append(client).append("\n");
        }
        return activeUser.toString();
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
