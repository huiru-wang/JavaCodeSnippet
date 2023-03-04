package com.snippet.javacodebase.concurrency.nio.client;

public class ClientStarter {

    public static void main(String[] args) {
        Client clientA = new Client(args[0]);
        clientA.startClient();
    }
}
