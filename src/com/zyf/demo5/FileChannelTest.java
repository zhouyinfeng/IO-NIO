package com.zyf.demo5;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
    // 文件的读写 主要是通过文件通道，结合RandomAccsesFile来共同完成。
    /**
     * java 字节流 首先要想到缓冲流，构建流的时候都需要用缓冲流来进行包装，目的 是提供IO流的读写性能。   java 字符流
     *
     * RandomAccsesFile 它可以进行读写操作，随机读写操作。 最高的一个算法:拆分文件读写。
     *
     */

    // 我们向工程目录下的a.txt文件写入一个字符串 hello suning

    // 创建一个文件通道
    public static void main(String[] args) throws FileNotFoundException {
        FileChannelTest.writeFile();
       // FileChannelTest.readFile();
    }

    private static void readFile() {
        try {
            //首先，把文件读出来
            RandomAccessFile rdf = new RandomAccessFile("a.txt","rw");
            //然后放在通道里
            FileChannel fileChannel = rdf.getChannel();
            //分配内存
            ByteBuffer bf = ByteBuffer.allocate(1024);
            //读出来
            int lines =  fileChannel.read(bf);
            //输出出来
            //之前要做个什么动作?0-limit
            //System.out.println("111111111");
            if (lines>0){
                bf.flip();
                System.out.println(new String(bf.array(),0,lines));
                fileChannel.close();
            }
            //hasRemaining() 会在释放缓冲区时告诉你是否已经达到缓冲区的上界
           /* while (bf.hasRemaining()){
                System.out.println((char)bf.get());
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeFile() throws FileNotFoundException {
        try {
            FileChannel fileChannel = null;
            RandomAccessFile raf = new RandomAccessFile("a.txt","rw");
            fileChannel = raf.getChannel();
            //往txt里写文件
            System.out.println("开始写文件");
            System.out.println(fileChannel.size());
            fileChannel.write(ByteBuffer.wrap("ni hao 你好 ".getBytes()));
            System.out.println(fileChannel.size());
            fileChannel.close();


            //继续写内容  如果不重新定义文件 会出现java.nio.channels.ClosedChannelException异常
            RandomAccessFile r = new RandomAccessFile("a.txt","rw");
            fileChannel = r.getChannel();
            System.out.println(fileChannel.size());
            fileChannel.position(fileChannel.size());
            fileChannel.write(ByteBuffer.wrap("success".getBytes()));
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
