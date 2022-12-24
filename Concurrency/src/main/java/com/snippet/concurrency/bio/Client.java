package com.snippet.concurrency.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        OutputStream outputStream = null;
        try (Socket socket = new Socket("127.0.0.1", 9999);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.length() > 0) {
                    out.println(line);
                }
            }
        } catch (Exception e) {
            System.out.println("connect failed");
        } finally {
            outputStream.close();
        }
    }
}
