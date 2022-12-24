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

/**
 * 写入断开的socket 会抛出java.io.IOException: Connection reset by peer
 */
public class SingleThreadNioServer {

    public static void main(String[] args) {
        //  打开一个socket fd文件
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            // 创建一个socket
            ServerSocket serverSocket = serverSocketChannel.socket();

            // 命名socket 绑定host:port, 并配置socket为非阻塞
            serverSocket.bind(new InetSocketAddress(9999));
            serverSocketChannel.configureBlocking(false); // 必须非阻塞，不然不可register

            // 打开多路复用选择器 默认epoll (epoll_create、epoll_ctl 向serverSocket绑定accept回调事件)
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("server start");
            for (; ; ) {
                // 阻塞调用 epoll_wait
                int event = selector.select();
                if (event == 0) {
                    continue;
                }
                // select 返回 取出就绪socket
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    if (selectionKey.isAcceptable()) {
                        // 客户端连接事件，创建channel并与之连接，向selector对此socket注册读写回调事件；
                        SocketChannel clientChannel = serverSocketChannel.accept();
                        SocketAddress clientInfo = clientChannel.getRemoteAddress();
                        System.out.println("client connected: " + clientInfo.toString());
                        clientChannel.configureBlocking(false);
                        // epoll_ctl
                        clientChannel.register(selector, SelectionKey.OP_READ);

                        try {
                            clientChannel.write(ByteBuffer.wrap("Join The Conversation".getBytes(StandardCharsets.UTF_8)));
                        } catch (IOException e) {
                            System.out.println("write data failed: " + e);
                        }

                    } else if (selectionKey.isReadable()) {
                        // 客户端读事件，从channel中读取消息，向selector对此socket注册读写回调事件；
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        SocketAddress socketAddress = socketChannel.getRemoteAddress();
                        try {
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            StringBuilder stringBuilder = new StringBuilder();
                            while (socketChannel.read(buffer) > 0) { // 已关闭则抛异常
                                buffer.flip();
                                stringBuilder.append(StandardCharsets.UTF_8.decode(buffer));
                                buffer.clear();
                            }
                            if (socketChannel.read(buffer) == -1) {
                                // 当客户端正常断开socket，读到-1，从epoll实例上取消事件
                                System.out.println("client disconnected");
                                selectionKey.cancel(); // epoll_ctl delete
                            }
                            System.out.println(socketAddress.toString() + ": " + stringBuilder);
                            castMessage(stringBuilder.toString(), selector.keys(), socketChannel);
                            // 不需要再次绑定 epoll实例上已经注册
                            // socketChannel.register(selector, SelectionKey.OP_READ);
                        } catch (Exception e) {
                            System.out.println(socketAddress.toString() + e);
                            // socket异常断开，取消epoll注册的事件，不再监听此socket
                            selectionKey.cancel();
                        }
                    }
                }
            }
        } catch (Exception e) {
            // selector 关闭 所有注册的资源都一并关闭
            System.out.println("server internal error : " + e);
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
