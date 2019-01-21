package com.redis.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RedisTest {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 6379);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("get wife\r\n".getBytes());
        outputStream.flush();
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int index = inputStream.read(bytes);
        System.out.println(new String(bytes, 0, index));
        System.out.println("end...");
    }
}
