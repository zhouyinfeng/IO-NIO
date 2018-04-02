package com.zyf.demo2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.security.Key;
import java.util.Iterator;

/**
 * 用thread 控制socket通信
 */
public class Server {
    public static void main(String[] args) {
        try {
            // 创建选择器selector
            Selector selector = Selector.open();
            //open server socket
            ServerSocketChannel channal = ServerSocketChannel.open();
            //綁定端口
            channal.socket().bind(new InetSocketAddress(5520));
            // 设置非阻塞
            channal.configureBlocking(false);
            // 这个时候我们去注册 选择器监听的事件
            // 1.请求 2.建立连接握手成功 3.读 4，写 事件
            channal.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                if (selector.select(5000) == 0) {
                    System.out.println("可以进行异步操作.........");
                    continue;
                }
                for (Iterator<SelectionKey> its = selector.selectedKeys().iterator(); its.hasNext(); ) {
                    SelectionKey key = its.next();
                    System.out.println("key "+key.readyOps());
                    if(key.isAcceptable()){
                        System.out.println("握手成功");
                        channal.accept();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
