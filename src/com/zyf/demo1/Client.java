package com.zyf.demo1;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * BIO 编程  重點注意  用完流需要close 不然會發不出去信息 出現timeout报错
 * @author zyf
 * */

public class Client {
    public static void sendSever(int port,String content){

        try {
            //客户端去请求服务端
            Socket socket = new Socket("127.0.0.1",port);

            //客户端向服务端发信息
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);

            pw.write("用戶名和密碼"+content);
            pw.flush();
            //禁用此套接字的输出流
            //socket半close 只关闭输出流（但输入流仍然能用）进入半写状态（write-half），通知对方远程输出已经关闭，
            socket.shutdownOutput();

            //客户端得到服务端的结果
            System.out.println("====================");
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            String line = null;
            while ((line=br.readLine())!=null){
                System.out.println("服務端發來的內容為："+line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        System.out.println("--------客户端开始启动-------");
        while (true){
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            Client.sendSever(5590,line);
        }
    }
}
