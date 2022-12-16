package com.snippet.concurrency.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public class NioServer {

    private static final Logger logger = Logger.getLogger(NioServer.class.getName());

    public static void main(String[] args) {
        //  打开一个socket fd文件
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            // 创建一个socket
            ServerSocket serverSocket = server.socket();

            // 命名socket 绑定host:port, 并配置socket为非阻塞
            InetSocketAddress address = new InetSocketAddress("localhost", 80);
            serverSocket.bind(address);
            server.configureBlocking(false);

            // 打开多路复用选择器 默认epoll (epoll_create、epoll_ctl 向serverSocket绑定accept回调事件)
            Selector selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);

            for (; ; ) {
                // 阻塞调用 epoll_wait
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    if (selectionKey.isAcceptable()) {
                        // 客户端连接事件，创建channel并与之连接，向selector对此socket注册读写回调事件；
                        ServerSocketChannel event = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel client = event.accept();
                        logger.info("client connect");
                        client.configureBlocking(false);
                        // epoll_ctl
                        client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

                    } else if (selectionKey.isReadable()) {
                        SocketChannel event = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        while (event.read(buffer) > 0) {
                     
                        }

                    } else if (selectionKey.isWritable()) {
                        SocketChannel event = (SocketChannel) selectionKey.channel();

                    }
                }
            }
        } catch (Exception e) {
            // selector 关闭 所有注册的资源都一并关闭
            logger.warning("server internal error : " + e);
        }
    }

}
