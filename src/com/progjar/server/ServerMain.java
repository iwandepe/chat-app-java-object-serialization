package com.progjar.server;

public class ServerMain {
    public static void main(String args[]) {
        ServerThread st = new ServerThread();
        st.start();
    }
}
