package com.snippet.concurrency.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 主线程线程为多路复用线程
 * 当select返回，将任务提交给线程池执行
 */
public class NioThreadPoolServer {
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(9999));
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            for (; ; ) {
                int event = selector.select();
                if (event == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isAcceptable()) {
                        SocketChannel clientChannel = serverSocketChannel.accept();
                        threadPool.execute(() -> {
                            try {
                                SocketAddress clientInfo = clientChannel.getRemoteAddress();
                                System.out.println("client connected: " + clientInfo.toString());
                                clientChannel.configureBlocking(false);
                                clientChannel.register(selector, SelectionKey.OP_READ);

                                clientChannel.write(ByteBuffer.wrap("Join The Conversation".getBytes(StandardCharsets.UTF_8)));
                            } catch (IOException e) {
                                System.out.println("write data failed: " + e);
                            }
                        });
                    } else if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        threadPool.execute(() -> {
                            try {
                                SocketAddress socketAddress = socketChannel.getRemoteAddress();
                                ByteBuffer buffer = ByteBuffer.allocate(1024);
                                StringBuilder stringBuilder = new StringBuilder();
                                while (socketChannel.read(buffer) > 0) { // 已关闭则抛异常
                                    buffer.flip();
                                    stringBuilder.append(StandardCharsets.UTF_8.decode(buffer));
                                    buffer.clear();
                                }
                                if (socketChannel.read(buffer) == -1) {
                                    System.out.println("client disconnected");
                                    selectionKey.cancel(); // epoll_ctl delete
                                }
                                System.out.println(socketAddress.toString() + ": " + stringBuilder);
                                castMessage(stringBuilder.toString(), selector.keys(), socketChannel);
                            } catch (Exception e) {
                                System.out.println(e);
                                selectionKey.cancel();
                            }
                        });
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("create server socket failed");
            e.printStackTrace();
        }

    }

    private static void castMessage(String message, Set<SelectionKey> keys, SocketChannel socketChannel) {
        for (SelectionKey key : keys) {
            SelectableChannel channel = key.channel();
            if (channel instanceof SocketChannel && !Objects.equals(channel, socketChannel)) {
                SocketChannel otherSocketChannel = (SocketChannel) channel;
                try {
                    otherSocketChannel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
                } catch (IOException e) {
                    System.out.println("cast message failed.");
                }
            }
        }
    }
}
