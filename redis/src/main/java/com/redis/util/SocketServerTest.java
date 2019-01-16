package com.redis.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket sc = new ServerSocket(6379);
        Socket socket = sc.accept();
        System.out.println("收到请求信号");
        InputStream is = socket.getInputStream();
        int index = 0;
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        index=is.read(bytes);
        baos.write(bytes, 0, index);
//        while ((index = is.read(bytes)) != -1) {
//            System.out.println("aaaaaa");
//        }
        String s = new String(baos.toByteArray(), "UTF-8");
        System.out.println(s);
//        Thread.sleep(10000);
        bytes=new byte[1024];
        index=is.read(bytes);
        baos=new ByteArrayOutputStream();
        baos.write(bytes,0,index);
        String s1=new String(baos.toByteArray());
        System.out.println(s1);
        while (true) {

        }
    }
}
