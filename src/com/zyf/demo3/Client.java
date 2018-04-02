package com.zyf.demo3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        while (true) {
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            sendMsg(line);
        }
    }

    private static void sendMsg(String line) {
        try {
            // 1.创建socket通道
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 5544));
            //2.設置非堵塞
            socketChannel.configureBlocking(false);

            //3、向服务器传送消息
            //把字符串转换为buffer ByteBuffer.wrap(line.getBytes())
            socketChannel.write(ByteBuffer.wrap(line.getBytes()));

            //4、开辟缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            while (true){
                byteBuffer.clear();
                // 读取服务器端传来的消息
                int readBytes = socketChannel.read(byteBuffer);
                if(readBytes>0){
                    byteBuffer.flip();
                    System.out.println("從服務端收到的信息為--->"+readBytes);
                    System.out.println("客户端收到的消息为:" + new String(byteBuffer.array(), 0, readBytes));
                    socketChannel.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
