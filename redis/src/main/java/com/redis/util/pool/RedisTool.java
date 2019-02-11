package com.redis.util.pool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RedisTool {
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private static final String RN = "\r\n";
    private static final String KONG_GE = " ";
    private static final String GET_PRE = "$-1";
    private static final String GET_RANGE_PRE = "$0";
    private static final String INCR_ERROR_PRE = "-ERR";

    public RedisTool(String host, int port) throws IOException {
        socket = new Socket(host, port);
        is = socket.getInputStream();
        os = socket.getOutputStream();
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

    public String get(String key) throws IOException {
        AssertParam(key);
        String command = "get " + key + RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        if (respStr.startsWith(GET_PRE)) {
            return null;
        }
        String[] resultArray = respStr.split(RN);
        return resultArray[1];
    }

    public String set(String key, String value) throws IOException {
        AssertParam(key);
        String command = "set " + key + KONG_GE + value + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    public String setnx(String key, String value) throws IOException {
        AssertParam(key);
        String command = "setnx " + key + KONG_GE + value + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    public String setex(String key, int seconds, String value) throws IOException {
        AssertParam(key);
        if (seconds <= 0) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        String command = "setex " + key + KONG_GE + seconds + KONG_GE + value + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    public String psetex(String key, int mseconds, String value) throws IOException {
        AssertParam(key);
        if (mseconds <= 0) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        String command = "psetex " + key + KONG_GE + mseconds + KONG_GE + value + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    public String getset(String key, String value) throws IOException {
        AssertParam(key);
        String command = "getset " + key + KONG_GE + value + RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        if (respStr.startsWith(GET_PRE)) {
            return null;
        }
        String[] resultArray = respStr.split(RN);
        return resultArray[1];
    }

    public String hset(String key, String field, Object value) throws IOException {
        AssertParam(key);
        AssertParam(field);
        String command = "hset " + key + KONG_GE + field + KONG_GE + value + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    public String hget(String key, String field) throws IOException {
        AssertParam(key);
        AssertParam(field);
        String command = "hget " + key + KONG_GE + field + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String respStr = new String(bytes);
        if (respStr.startsWith(GET_PRE)) {
            return null;
        }
        String[] resultArray = respStr.split(RN);
        return resultArray[1];
    }

    public int strlen(String key) throws IOException {
        AssertParam(key);
        String command = "strlen " + key + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String respStr = new String(bytes);
        String[] resultArray = respStr.split(RN);
        return Integer.valueOf(resultArray[0].substring(1));
    }

    public int append(String key, String value) throws IOException {
        AssertParam(key);
        String command = "append " + key + KONG_GE + value + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String respStr = new String(bytes);
        String[] resultArray = respStr.split(RN);
        return Integer.valueOf(resultArray[0].substring(1));
    }

    public int setrange(String key, int offset, String value) throws IOException {
        AssertParam(key);
        String command = "setrange " + key + KONG_GE + offset + KONG_GE + value + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String respStr = new String(bytes);
        String[] resultArray = respStr.split(RN);
        return Integer.valueOf(resultArray[0].substring(1));
    }

    public String getrange(String key, int start, int end) throws IOException {
        AssertParam(key);
        String command = "getrange " + key + KONG_GE + start + KONG_GE + end + RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        if (respStr.startsWith(GET_RANGE_PRE)) {
            return null;
        }
        String[] resultArray = respStr.split(RN);
        return resultArray[1];
    }

    public int incr(String key) throws IOException {
        AssertParam(key);
        String command = "incr " + key + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        if (resultStr.startsWith(INCR_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        int index = resultStr.lastIndexOf(RN);
        return Integer.valueOf(resultStr.substring(1, index));
    }

    public int incrby(String key, int increment) throws IOException {
        AssertParam(key);
        String command = "incrby " + key + KONG_GE + increment + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        if (resultStr.startsWith(INCR_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        int index = resultStr.lastIndexOf(RN);
        return Integer.valueOf(resultStr.substring(1, index));
    }

    public float incrbyfloat(String key, float increment) throws IOException {
        AssertParam(key);
        String command = "incrbyfloat " + key + KONG_GE + increment + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        if (resultStr.startsWith(INCR_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        String[] resultArray = resultStr.split(RN);
        return Float.valueOf(resultArray[1]);
    }

    public int decr(String key) throws IOException {
        AssertParam(key);
        String command = "decr " + key + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        if (resultStr.startsWith(INCR_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        int index = resultStr.lastIndexOf(RN);
        return Integer.valueOf(resultStr.substring(1, index));
    }

    public int decrby(String key, int increment) throws IOException {
        AssertParam(key);
        String command = "decrby " + key + KONG_GE + increment + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        if (resultStr.startsWith(INCR_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        int index = resultStr.lastIndexOf(RN);
        return Integer.valueOf(resultStr.substring(1, index));
    }

    public String mset(String... var) throws IOException {
        if (var.length == 0 || var.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        String command = "mset ";
        for (int i = 0; i < var.length - 1; i += 2) {
            String key = var[i];
            String value = var[i + 1];
            AssertParam(key);
            command += (key + KONG_GE + value + KONG_GE);
        }
        command.trim();
        command += RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    public static void main(String[] args) throws IOException {
        RedisTool redisTool = new RedisTool("127.0.0.1", 6379);
        System.out.println(redisTool.mset("name1", "jack1", "name2", "jack2"));
        System.out.println(redisTool.incrbyfloat("dadada", 3.6f));
        System.out.println(redisTool.getrange("sun", 1, 4));
        System.out.println(redisTool.setrange("sun", 1, "test"));
        System.out.println(redisTool.get("sun"));
        System.out.println(redisTool.append("sun345235", "mayun"));
        System.out.println(redisTool.strlen("sun"));
        System.out.println(redisTool.get("sun"));
        System.out.println(redisTool.set("sun", "wurenjie2"));
        System.out.println(redisTool.get("sun"));
//        System.out.println(redisTool.hset("mulu", "wrj-niu", "jack-ma"));
        System.out.println(redisTool.hget("mulu", "wrj"));
        System.out.println(redisTool.setnx("sun1", "wrj-setnx1"));
        System.out.println(redisTool.get("sun1"));
        System.out.println(redisTool.setex("test-ex", 20, "test-ee"));
        System.out.println(redisTool.get("test-pex"));
        System.out.println("psetex:" + redisTool.psetex("test-pex", 20000, "pex-ttl"));
        redisTool.set("getset-test", "old-value");
        System.out.println(redisTool.get("getset-test"));
        System.out.println("getset:" + redisTool.getset("getset-test", "new-value"));
        System.out.println(redisTool.get("getset-test"));
    }
}
