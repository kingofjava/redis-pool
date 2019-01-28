package com.redis.util.pool;

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
        AssertParam(key);
        String command = "get " + key + "\r\n";
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        if (respStr.startsWith("$-1")) {
            return null;
        }
        String[] resultArray = respStr.split("\r\n");
        return resultArray[1];
    }

    private void sendCommand(String command) throws IOException {
        os.write(command.getBytes());
        os.flush();
    }

    private byte[] getResult() throws IOException {
        int len = 0;
        while (len == 0) {
            len = is.available();
        }
        byte[] bytes = new byte[len];
        is.read(bytes);
        return bytes;
    }

    private void AssertParam(String param) {
        if (null == param) {
            throw new IllegalArgumentException();
        }
    }

    public String set(String key, String value) throws IOException {
        AssertParam(key);
        String command = "set " + key + " " + value + "\r\n";
        sendCommand(command);
        byte[] bytes=getResult();
        String resultStr=new String(bytes);
        int index=resultStr.lastIndexOf("\r\n");
        return resultStr.substring(1,index);
    }

    public static void main(String[] args) throws IOException {
        RedisTool redisTool = new RedisTool("127.0.0.1", 6379);
        System.out.println(redisTool.get("sun"));
        System.out.println(redisTool.set("sun","wurenjie2"));
        System.out.println(redisTool.get("sun"));
    }
}
