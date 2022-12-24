package com.snippet.concurrency.bio;

import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("client connected");

                ServerHandler serverHandler = new ServerHandler(socket);
                new Thread(serverHandler).start();

            }
        } catch (Exception e) {
            System.out.println("server internal error");
        }
    }
}
