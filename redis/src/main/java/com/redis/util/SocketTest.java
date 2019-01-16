package com.redis.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("start...");
        Socket socket = new Socket("127.0.0.1",6379);
//        Socket socket = new Socket();
//        InetSocketAddress isa = new InetSocketAddress("127.0.0.1",6379);
        OutputStream os = socket.getOutputStream();
        os.write("老子一定能搞定的哈哈哈哈哈哈哈".getBytes());
        os.flush();
        System.out.println("end.....");
        Socket s1=new Socket("127.0.0.1",6379);
        Thread.sleep(1000);
        OutputStream os1=s1.getOutputStream();
        os1.write("再搞你一次。。。。。".getBytes());
        os1.flush();
        System.out.println("end2....");


        while (true){

        }
    }
}
