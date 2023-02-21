package com.snippet.base.concurrency.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class ServerHandler implements Runnable {

    private final Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
             PrintWriter printWriter = new PrintWriter(this.socket.getOutputStream(), true)) {
            String body;
            while ((body = bufferedReader.readLine()) != null && body.length() != 0) {
                System.out.println("receive msg: " + body);
                printWriter.println(new Date().toString());
            }
        } catch (Exception e) {
            System.out.println("client disconnected");
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("socket close failed");
            }
        }
    }
}
