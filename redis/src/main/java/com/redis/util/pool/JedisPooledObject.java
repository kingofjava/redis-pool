package com.redis.util.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectState;
import redis.clients.jedis.Jedis;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Deque;

public class JedisPooledObject implements org.apache.commons.pool2.PooledObject<Jedis>{
    private Jedis object;
    private Date createTime;
    private long activeTimeMillis;
    
    @Override
    public Jedis getObject() {
        return object;
    }

    @Override
    public long getCreateTime() {
        return createTime.getTime();
    }

    @Override
    public long getActiveTimeMillis() {
        return 0;
    }

    @Override
    public long getIdleTimeMillis() {
        return 0;
    }

    @Override
    public long getLastBorrowTime() {
        return 0;
    }

    @Override
    public long getLastReturnTime() {
        return 0;
    }

    @Override
    public long getLastUsedTime() {
        return 0;
    }

    @Override
    public int compareTo(PooledObject<Jedis> pooledObject) {
        return 0;
    }

    @Override
    public boolean startEvictionTest() {
        return false;
    }

    @Override
    public boolean endEvictionTest(Deque<PooledObject<Jedis>> deque) {
        return false;
    }

    @Override
    public boolean allocate() {
        return false;
    }

    @Override
    public boolean deallocate() {
        return false;
    }

    @Override
    public void invalidate() {

    }

    @Override
    public void setLogAbandoned(boolean b) {

    }

    @Override
    public void use() {

    }

    @Override
    public void printStackTrace(PrintWriter printWriter) {

    }

    @Override
    public PooledObjectState getState() {
        return null;
    }

    @Override
    public void markAbandoned() {

    }

    @Override
    public void markReturning() {

    }
}
