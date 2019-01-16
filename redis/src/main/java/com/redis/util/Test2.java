package com.redis.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Test2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket ss1=new ServerSocket(6379);
        Thread.sleep(15000);
        System.out.println("begin to 接受。。。。。。");
        Socket socket = ss1.accept();
        InputStream is=socket.getInputStream();
        byte[] bytes=new byte[1024];
        int index=is.read(bytes);
        System.out.println(new String(bytes,0,index));
        Thread.sleep(3000);
        Socket socket1=ss1.accept();
        InputStream is2 = socket1.getInputStream();
        byte[] bytes2 = new byte[1024];
        int index2=is2.read(bytes2);
        System.out.println(new String(bytes2,0,index2));
        while (true){

        }
    }
}
