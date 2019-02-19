package com.redis.util.pool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
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
    private static final String DATA_0 = "*0";
    private static final String NX = "NX";
    private static final String PX = "PX";
    private static final String EX = "EX";
    private static final String XX = "XX";


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

    public String set(String key, String value, String nxxx) throws IOException {
        AssertParam(key);
        AssertParam(value);
        if (!NX.equalsIgnoreCase(nxxx) && !XX.equalsIgnoreCase(nxxx)) {
            throw new IllegalArgumentException();
        }
        String command = "set " + key + KONG_GE + value + KONG_GE + nxxx + RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        int index = resultStr.lastIndexOf(RN);
        return resultStr.substring(1, index);
    }

    public String set(String key, String value, String nxxx, String expx, int time) throws IOException {
        AssertParam(key);
        AssertParam(value);
        if (!NX.equalsIgnoreCase(nxxx) && !XX.equalsIgnoreCase(nxxx)) {
            throw new IllegalArgumentException();
        }
        if (!EX.equalsIgnoreCase(expx) && !PX.equalsIgnoreCase(expx)) {
            throw new IllegalArgumentException();
        }
        if (time <= 0) {
            throw new IllegalArgumentException();
        }
        String command = "set " + key + KONG_GE + value + KONG_GE + nxxx + KONG_GE + expx + KONG_GE + time + RN;
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
     *
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

    /**
     * 返回列表 key 中，下标为 index 的元素。
     * <p>
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * <p>
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * <p>
     * 如果 key 不是列表类型，返回一个错误。
     *
     * @param key
     * @param index
     * @return
     * @throws IOException
     */
    public String lindex(String key, int index) throws IOException {
        AssertParam(key);
        String command = "lindex " + key + KONG_GE + index + RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        if (respStr.startsWith(LLEN_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        if (respStr.startsWith(GET_PRE)) {
            return null;
        }
        String[] resultArray = respStr.split(RN);
        return resultArray[1];
    }

    /**
     * 将值 value 插入到列表 key 当中，位于值 pivot 之前或之后。
     * <p>
     * 当 pivot 不存在于列表 key 时，不执行任何操作。
     * <p>
     * 当 key 不存在时， key 被视为空列表，不执行任何操作。
     * <p>
     * 如果 key 不是列表类型，返回一个错误。
     * <p>
     * 返回值
     * 如果命令执行成功，返回插入操作完成之后，列表的长度。 如果没有找到 pivot ，返回 -1 。 如果 key 不存在或为空列表，返回 0 。
     *
     * @param key
     * @param where
     * @param povit
     * @param value
     * @return
     * @throws IOException
     */
    public Long linsert(String key, POSITION where, String povit, String value) throws IOException {
        AssertParam(key);
        AssertParam(povit);
        AssertParam(value);
        String command = "linsert " + key + KONG_GE + where.getDesc() + KONG_GE + povit + KONG_GE + value + RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        if (respStr.startsWith(LLEN_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        int index = respStr.lastIndexOf(RN);
        return Long.valueOf(respStr.substring(1, index));
    }

    /**
     * 将列表 key 下标为 index 的元素的值设置为 value 。
     * <p>
     * 当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，返回一个错误。
     * <p>
     * 关于列表下标的更多信息，请参考 LINDEX key index 命令。
     * <p>
     * 返回值
     * 操作成功返回 ok ，否则返回错误信息。
     *
     * @param key
     * @param index
     * @param value
     * @return
     * @throws IOException
     */
    public String lset(String key, int index, String value) throws IOException {
        AssertParam(key);
        AssertParam(value);
        String command = "lset " + key + KONG_GE + index + KONG_GE + value + RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        if (respStr.startsWith(INCR_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        int indexStart = respStr.lastIndexOf(RN);
        return respStr.substring(1, indexStart);
    }

    /**
     * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
     * <p>
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * <p>
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param key
     * @param start
     * @param stop
     * @return
     * @throws IOException
     */
    public List<String> lrange(String key, int start, int stop) throws IOException {
        AssertParam(key);
        String command = "lrange " + key + KONG_GE + start + KONG_GE + stop + RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        String[] resultArray = respStr.split(RN);
        if (respStr.startsWith(LLEN_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        if (respStr.startsWith(DATA_0)) {
            return null;
        }
        List<String> valueList = new ArrayList<>();
        for (int i = 1; i < resultArray.length; ) {
            valueList.add(resultArray[i + 1]);
            i += 2;
        }
        return valueList;
    }

    /**
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     * <p>
     * 举个例子，执行命令 LTRIM list 0 2 ，表示只保留列表 list 的前三个元素，其余元素全部删除。
     * <p>
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * <p>
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * <p>
     * 当 key 不是列表类型时，返回一个错误。
     *
     * @param key
     * @param start
     * @param stop
     * @return
     * @throws IOException
     */
    public String ltrim(String key, int start, int stop) throws IOException {
        AssertParam(key);
        String command = "ltrim " + key + KONG_GE + start + KONG_GE + stop + RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        if (respStr.startsWith(LLEN_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        int indexStart = respStr.lastIndexOf(RN);
        return respStr.substring(1, indexStart);
    }

    //todo blpop,brpop,brpoplpush命令

    public Long sadd(String key, String... values) throws IOException {
        AssertParam(key);
        if (null == values || values.length == 0) {
            // todo 换一个异常
            throw new IllegalArgumentException();
        }
        for (String v : values) {
            AssertParam(v);
        }
        String command = "sadd " + key + KONG_GE;
        for (String v : values) {
            command += (v + KONG_GE);
        }
        command = command.trim();
        command += RN;
        sendCommand(command);
        byte[] bytes = getResult();
        String resultStr = new String(bytes);
        if (resultStr.startsWith(LLEN_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        int index = resultStr.lastIndexOf(RN);
        return Long.valueOf(resultStr.substring(1, index));
    }

    public boolean sismember(String key, String value) throws IOException {
        AssertParam(key);
        AssertParam(value);
        String command = "sismember " + key + KONG_GE + value + RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        if (respStr.startsWith(LLEN_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        int index = respStr.lastIndexOf(RN);
        return "1".equals(respStr.substring(1, index));
    }

    public List<String> spop(String key, int count) throws IOException {
        AssertParam(key);
        if (count <= 0) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        String command = "spop " + key + KONG_GE + count + RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        if (respStr.startsWith(LLEN_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        String[] resList = respStr.split(RN);
        int resLength = Integer.valueOf(resList[0].substring(1));
        List<String> result = new LinkedList<>();
        if (resLength == 0) {
            return null;
        } else {
            for (int i = 0; i < resLength; i++) {
                result.add(resList[2 * i + 2]);
            }
        }
        return result;
    }

    public List<String> srandmember(String key, int count) throws IOException {
        AssertParam(key);
        if (count <= 0) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        String command = "srandmember " + key + KONG_GE + count + RN;
        sendCommand(command);
        byte[] resultBytes = getResult();
        String respStr = new String(resultBytes);
        if (respStr.startsWith(LLEN_ERROR_PRE)) {
            //todo 换一个异常
            throw new IllegalArgumentException();
        }
        String[] resList = respStr.split(RN);
        int resLength = Integer.valueOf(resList[0].substring(1));
        List<String> result = new LinkedList<>();
        if (resLength == 0) {
            return null;
        } else {
            for (int i = 0; i < resLength; i++) {
                result.add(resList[2 * i + 2]);
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
//        Jedis jedis = new Jedis("127.0.0.1", 6379);
//        jedis.sadd("0219set", null);
        RedisTool redisTool = new RedisTool("127.0.0.1", 6379);
        System.out.println(redisTool.srandmember("0219set", 2));
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
