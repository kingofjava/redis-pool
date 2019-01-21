package com.redis.util.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;

public class WJedisPool {
    private GenericObjectPool<Jedis> objectPool;

    public WJedisPool(GenericObjectPoolConfig objectPoolConfig, String host) {
        this.objectPool = new GenericObjectPool<>(new JedisPooledObjectFactory(host, 6379), objectPoolConfig);
    }

    public Jedis getResource () throws Exception {
        return this.objectPool.borrowObject();
    }
}
