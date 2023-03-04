package com.snippet.javacodebase.concurrency.nio.client;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class ClientService implements Runnable {

    private final Selector selector;

    public ClientService(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            for (; ; ) {
                this.selector.select();

                Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        socketChannel.configureBlocking(false);
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        StringBuilder stringBuilder = new StringBuilder();
                        while (socketChannel.read(buffer) > 0) {
                            buffer.flip();
                            stringBuilder.append(StandardCharsets.UTF_8.decode(buffer));
                            buffer.clear();
                        }
                        System.out.println(stringBuilder);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
