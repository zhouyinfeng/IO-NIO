package com.zyf.demo2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args){

    //open channel

        try {
            SocketChannel socketChannel = SocketChannel.open();
            //set unblocking
            socketChannel.configureBlocking(false);
            //connect server
            socketChannel.connect(new InetSocketAddress("127.0.0.1",5520));
            System.out.println("client  connect server");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
