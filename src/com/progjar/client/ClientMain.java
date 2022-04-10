package com.progjar.client;

import com.progjar.object.Message;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientMain {
    public static void main(String args[]) {
        final String[] users = {"alice", "bob", "celine"};
        final String[] commands = {"/send", "/bc", "/close", "/help", "/active"};

        try {
            Socket socket = new Socket("127.0.0.1", 9000);

            ObjectOutputStream ous = new ObjectOutputStream(socket.getOutputStream());
            ClientWorker wt = new ClientWorker(new ObjectInputStream(socket.getInputStream()));
            wt.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("enter your username: ");
            String username = reader.readLine();
            while (!Arrays.stream(users).anyMatch(username::equals)) {
                System.out.println("wrong username!");
                System.out.println("enter your username: ");
                username = reader.readLine();
            }

            Message message = new Message();
            message.setSender(username);
            message.setReceiver("system");
            message.setText("login");

            ous.writeObject(message);
            ous.flush();

            System.out.println("type command: (type /help for info)");
            String input = reader.readLine();

            while(!input.equals("/close")) {
                message = new Message();
                message.setSender(username);

                String[] words = input.split(" ");

                if (!Arrays.stream(commands).anyMatch(words[0]::equals)) {
                    System.out.println("command not valid!");
                    System.out.println("type command: ");
                    input = reader.readLine();
                    continue;
                }

                if (words[0].equals("/help")) {
                    System.out.println("\t\t\tUSER COMMANDS");
                    System.out.println("/send");
                    System.out.println("\tdescription\t: send message to spesific user");
                    System.out.println("\tformat\t\t: /send {username} {messaage}");
                    System.out.println("\texample\t\t: /send bob Hello bob!");
                    System.out.println();
                    System.out.println("/bc");
                    System.out.println("\tdescription\t: send message to all online users");
                    System.out.println("\tformat\t\t: /bc {message}");
                    System.out.println("\texample\t\t: /bc Hai all!");
                    System.out.println();
                    System.out.println("/close");
                    System.out.println("\tdescription\t: close connection to the server");
                    System.out.println();
                    System.out.println("/help");
                    System.out.println("\tdescription\t: see manual for commands");
                    System.out.println();

                    System.out.println("type command: ");
                    input = reader.readLine();
                    continue;
                }

                if (words[0].equals("/send")) {
                    message.setReceiver(words[1]);
                    message.setText(input.substring(words[0].length() + words[1].length() + 2));

                    if (!message.getReceiver().equals(username)) {
                        System.out.println("type command: ");
                    }
                }

                if (words[0].equals("/bc")) {
                    message.setReceiver("all");
                    message.setText(input.substring(words[0].length() + 1));
                }

                if (words[0].equals("/active")) {
                    message.setReceiver(username);
                    message.setText("<active>");
                }

                ous.writeObject(message);
                ous.flush();

                input = reader.readLine();
            }


            wt.interrupt();

            message.setReceiver("system");
            message.setText("close");

            ous.writeObject(message);
            ous.flush();

            ous.close();
            socket.close();

            System.out.println("my socket is closed");

        } catch (IOException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
