package com.redis.util.pool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RedisTool {
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private static final String RN = "\r\n";
    private static final String KONG_GE = " ";
    private static final String GET_PRE = "$-1";
    private static final String GET_RANGE_PRE = "$0";
    private static final String INCR_ERROR_PRE = "-ERR";
    private static final String LLEN_ERROR_PRE = "-WRONGTYPE";

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

    private void AssertParam(Object param) {
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
        AssertParam(value);
        String command = "set " + key + KONG_GE + value + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    public String setnx(String key, String value) throws IOException {
        AssertParam(key);
        AssertParam(value);
        String command = "setnx " + key + KONG_GE + value + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    public String setex(String key, int seconds, String value) throws IOException {
        AssertParam(key);
        AssertParam(value);
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
        AssertParam(value);
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
        AssertParam(value);
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
        AssertParam(value);
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
        AssertParam(value);
        String command = "append " + key + KONG_GE + value + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String respStr = new String(bytes);
        String[] resultArray = respStr.split(RN);
        return Integer.valueOf(resultArray[0].substring(1));
    }

    public int setrange(String key, int offset, String value) throws IOException {
        AssertParam(key);
        AssertParam(value);
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
        command = appendmCommand(command, var);
        command = command.trim();
        command += RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    public String msetnx(String... var) throws IOException {
        if (var.length == 0 || var.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        String command = "msetnx ";
        command = appendmCommand(command, var);
        command = command.trim();
        command += RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    public String appendmCommand(String command, String[] var) {
        for (int i = 0; i < var.length - 1; i += 2) {
            String key = var[i];
            String value = var[i + 1];
            AssertParam(key);
            command += (key + KONG_GE + value + KONG_GE);
        }
        return command;
    }

    /**
     * 一次获取多个key的value
     *
     * @param keys
     * @return
     * @throws IOException
     */
    public List<String> mget(String... keys) throws IOException {
        if (null == keys || keys.length == 0) {
            throw new IllegalArgumentException();
        }
        for (String key : keys) {
            AssertParam(key);
        }
        String command = "mget ";
        for (String key : keys) {
            command += key + KONG_GE;
        }
        command = command.trim();
        command += RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        String[] resultArray = respStr.split(RN);
        List<String> valueList = new ArrayList<>();
        for (int i = 1; i < resultArray.length; ) {
            if (GET_PRE.equals(resultArray[i])) {
                valueList.add(null);
                i++;
            } else {
                valueList.add(resultArray[i + 1]);
                i += 2;
            }
        }
        return valueList;
    }

    /**
     * list数据结构。从左边push
     *
     * @param key
     * @param values
     * @return
     */
    public String lpush(String key, String... values) throws IOException {
        AssertParam(key);
        if (null == values || values.length == 0) {
            throw new IllegalArgumentException();
        }
        for (String v : values) {
            AssertParam(v);
        }
        String command = "lpush " + key + KONG_GE;
        for (String v : values) {
            command += (v + KONG_GE);
        }
        command = command.trim();
        command += RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    /**
     * list数据结构。从左边push,key不存在则do nothing
     *
     * @param key
     * @param value
     * @return
     */
    public String lpushx(String key, String value) throws IOException {
        AssertParam(key);
        AssertParam(value);
        String command = "lpushx " + key + KONG_GE + value + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    /**
     * list数据结构。从右边push
     *
     * @param key
     * @param values
     * @return
     */
    public String rpush(String key, String... values) throws IOException {
        AssertParam(key);
        if (null == values || values.length == 0) {
            throw new IllegalArgumentException();
        }
        for (String v : values) {
            AssertParam(v);
        }
        String command = "rpush " + key + KONG_GE;
        for (String v : values) {
            command += (v + KONG_GE);
        }
        command = command.trim();
        command += RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    /**
     * list数据结构。从右边push,key不存在则do nothing
     *
     * @param key
     * @param value
     * @return
     */
    public String rpushx(String key, String value) throws IOException {
        AssertParam(key);
        AssertParam(value);
        String command = "rpushx " + key + KONG_GE + value + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    /**
     * 从list左边获取并删除该元素
     *
     * @param key
     * @return
     * @throws IOException
     */
    public String lpop(String key) throws IOException {
        AssertParam(key);
        String command = "lpop " + key + RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        if (respStr.startsWith(GET_PRE)) {
            return null;
        }
        String[] resultArray = respStr.split(RN);
        return resultArray[1];
    }

    /**
     * 从list左边获取并删除该元素
     *
     * @param key
     * @return
     * @throws IOException
     */
    public String rpop(String key) throws IOException {
        AssertParam(key);
        String command = "rpop " + key + RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        if (respStr.startsWith(GET_PRE)) {
            return null;
        }
        String[] resultArray = respStr.split(RN);
        return resultArray[1];
    }

    /**
     * 从list srcKey的右边获取元素ele并删除。将ele从左边push到dstKey。返回ele。 srcKey不存在则为null. dstKey不存在则创建一个并push.
     *
     * @param srcKey
     * @param dstKey
     * @return
     * @throws IOException
     */
    public String rpoplpush(String srcKey, String dstKey) throws IOException {
        AssertParam(srcKey);
        AssertParam(dstKey);
        String command = "rpoplpush " + srcKey + KONG_GE + dstKey + RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        if (respStr.startsWith(GET_PRE)) {
            return null;
        }
        String[] resultArray = respStr.split(RN);
        return resultArray[1];
    }

    /**
     * 对list
     * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
     * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
     * count = 0 : 移除表中所有与 value 相等的值。
     *
     * @param key
     * @param count
     * @param value
     * @return
     * @throws IOException
     */
    public String lrem(String key, int count, String value) throws IOException {
        AssertParam(key);
        AssertParam(value);
        String command = "lrem " + key + KONG_GE + count + KONG_GE + value + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    /**
     * 获取list 的长度
     * @param key
     * @return
     * @throws IOException
     */
    public int llen(String key) throws IOException {
        AssertParam(key);
        String command = "llen " + key + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        if (resultStr.startsWith(LLEN_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        int index = resultStr.lastIndexOf(RN);
        return Integer.valueOf(resultStr.substring(1, index));
    }

    public static void main(String[] args) throws IOException {
        RedisTool redisTool = new RedisTool("127.0.0.1", 6379);
        System.out.println(redisTool.llen("jjj"));
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
