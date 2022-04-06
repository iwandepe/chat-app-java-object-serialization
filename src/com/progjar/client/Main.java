package com.progjar.client;

import com.progjar.object.Message;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String args[]) {
        try {
            Socket socket = new Socket("127.0.0.1", 9000);

            ObjectOutputStream ous = new ObjectOutputStream(socket.getOutputStream());
            WorkerThread wt = new WorkerThread(new ObjectInputStream(socket.getInputStream()));
            wt.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Enter your username: ");
            String username = reader.readLine();
            while(true) {
                Message message = new Message();
                message.setSender(username);

                System.out.println("Enter your message: ");
                String text = reader.readLine();
                message.setText(text);

                ous.writeObject(message);
                ous.flush();
            }

//            unreacable statements
//            ous.close();
//            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
