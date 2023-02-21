package com.snippet.base.concurrency.nio.client;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    private final String clientName;

    public Client(String clientName) {
        this.clientName = clientName;
    }

    public void startClient() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            // 非阻塞 连接失败则退出
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999));
            socketChannel.configureBlocking(false); // 必须非阻塞，不然不可register

            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ);

            ClientService clientService = new ClientService(selector);
            new Thread(clientService).start();

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.length() > 0) {
                    String message = this.clientName + ":" + line;
                    socketChannel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
                }
            }
        } catch (Exception e) {
            System.out.println("client close");
            e.printStackTrace();
        }
    }
}
