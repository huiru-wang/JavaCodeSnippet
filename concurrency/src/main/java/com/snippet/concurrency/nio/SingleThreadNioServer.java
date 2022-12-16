package com.snippet.concurrency.nio;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class SingleThreadNioServer {

    public static void main(String[] args) {
        //  打开一个socket fd文件
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            // 创建一个socket
            ServerSocket serverSocket = serverSocketChannel.socket();

            // 命名socket 绑定host:port, 并配置socket为非阻塞
            serverSocket.bind(new InetSocketAddress(9999));
            serverSocketChannel.configureBlocking(false);

            // 打开多路复用选择器 默认epoll (epoll_create、epoll_ctl 向serverSocket绑定accept回调事件)
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("server start");
            for (; ; ) {
                // 阻塞调用 epoll_wait
                selector.select();
                // select 返回 取出就绪socket
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    if (selectionKey.isAcceptable()) {
                        // 客户端连接事件，创建channel并与之连接，向selector对此socket注册读写回调事件；
                        ServerSocketChannel socketChannel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel clientChannel = socketChannel.accept();
                        System.out.println("client connected");
                        clientChannel.configureBlocking(false);
                        // epoll_ctl
                        clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        clientChannel.write(ByteBuffer.wrap("hello\n".getBytes(StandardCharsets.UTF_8)));

                    } else if (selectionKey.isReadable()) {
                        // 客户端读事件，从channel中读取消息，向selector对此socket注册读写回调事件；
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        StringBuilder stringBuilder = new StringBuilder();
                        while (channel.read(buffer) > 0) {
                            buffer.flip();
                            stringBuilder.append(StandardCharsets.UTF_8.decode(buffer));
                            buffer.clear();
                        }
                        log.info("client message: {}", stringBuilder);
                        System.out.println(stringBuilder);
                        // 不需要再次绑定 epoll实例上已经注册
                        // channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

                    }
                }
            }
        } catch (Exception e) {
            // selector 关闭 所有注册的资源都一并关闭
            System.out.println("server internal error : " + e);
        }
    }

}
