package com.zyf.demo1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
   /* private static ServerSocket serverSocket;*/

    public static void main(String[] args){
        try {
            System.out.println("服务器端开始监听---------");
            ServerSocket serverSocket = new ServerSocket(5590);
            //System.out.println("服务器端已经启动,端口号为:"+5590);
            //重点 服务器始终保持监听
            while (true){
                Socket socket= serverSocket.accept();;
                BufferedReader bf=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String info = null;
                while ((info = bf.readLine())!=null){
                    System.out.println("收到客户端发来的信息为："+info);
                }
                // 只关闭输入流（但输出流仍然能用）进入半读状态（read-half），通知对方远程读取已经关闭
                socket.shutdownInput();

                //服務端發送的內容
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.write("我是服務端發送的內容");
                out.flush();
                out.close();
                bf.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
