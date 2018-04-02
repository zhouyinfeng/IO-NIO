package com.zyf.demo3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
/*
//bytebuffer和byte的转化  bytebuffer到byte 用array   byte到bytebuffer 用wrap
//string和byte的转化  string-->byte string.getbytes  byte--->string 用new string(byte,beginlength,endlength)
*/
public class Server implements Runnable {
    @Override
    public void run() {
        try {
            // 1、首先打开服务器端通道的选择器
            Selector selector = Selector.open();
            // 2.在服务器端打开通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 3.设置通道非阻塞
            serverSocketChannel.configureBlocking(false);
            //4.通道绑定
            serverSocketChannel.socket().bind(new InetSocketAddress(5544));
            // 5.向通道注册对应的事件标示
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("服务器已经启动，" + "并在服务器上注册了扫描事件的模型...");

            // 开始轮询扫描[1,有客户端请求连接并握手成功 2.没有客户端请求]
            while (true) {
                int keys = selector.select(13000);
                if (keys == 0) {
                    System.out.println("在没有客户端请求的时候，可以做一些其他的事情。。。。");
                    continue;
                }

                //如果有客戶請求
                if (keys > 0) {
                    // 我们就需要处理客户端的请求
                    // 我们收集客户端请求的事件key的集合
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    //得到迭代器對象
                    Iterator<SelectionKey> its = selectionKeys.iterator();
                    while (its.hasNext()) {
                        // 获取事件的key值，客户端请求对应的事件的key值
                        SelectionKey key = its.next();

                        if (key.isAcceptable()) {
                            System.out.println(Thread.currentThread().getName() + ",服务器端已经同意握手，" + "建立了传输通道....");

                            handleAccept(key);
                        }
                        if (key.isReadable()) {
                            System.out.println(Thread.currentThread().getName() + ",服务器端准备读取客户端传输的数据....");
                            handlerReader(key);
                        }
                        //每次这个事件类型的键值要移除掉 important
                        its.remove();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 服务器端读取客户端read
    private void handlerReader(SelectionKey key) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        SocketChannel socketChannel = (SocketChannel) key.channel();
        while (true){
                int rkey = socketChannel.read(byteBuffer);
            if(rkey>0){
                System.out.println("服务器端rkey:" + rkey);
                System.out.println("服务器端接受的消息为:" +
                        new String(byteBuffer.array(), 0, rkey));

                //服务器端接收到客户端的数据，相应的处理
                //直接在这里下发消息给客户端
                byteBuffer.flip();
                socketChannel.write(ByteBuffer.wrap("hello clink".getBytes()));
                break;
            }
        }
        socketChannel.close();
    }


    // 服务器端和客户端握手成功，建立传输通道，并注册传输的事件key
    private void handleAccept(SelectionKey key) {
        // 创建通道
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        // 接受这个请求到Channel
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();

            // 设置scoketChannel非阻塞
            socketChannel.configureBlocking(false);
            // 注册准备读取事件，准备读客户端传输的数据
            socketChannel.register(key.selector(), SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new Server()).start();
    }
}
