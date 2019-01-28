package com.redis.util.pool;

import jdk.internal.util.xml.impl.Input;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RedisTool {
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    public RedisTool(String host, int port) throws IOException {
        socket = new Socket(host, port);
        is = socket.getInputStream();
        os = socket.getOutputStream();
    }

    public String get(String key) throws IOException {
        String command="get "+key+"\r\n";
        os.write(command.getBytes());
        os.flush();
        byte[] bytes =new byte[8*1024];
        int index=is.read(bytes);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        baos.write(bytes,0,index);
        String respStr=new String(baos.toByteArray());
        if(respStr.startsWith("$-1")){
            return null;
        }
        String[] resultArray=respStr.split("\r\n");
        return resultArray[1];
    }

    public static void main(String[] args) throws IOException {
        RedisTool redisTool = new RedisTool("127.0.0.1", 6379);
        System.out.println(redisTool.get("sun"));
    }
}
