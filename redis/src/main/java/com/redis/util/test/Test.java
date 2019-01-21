package com.redis.util.test;

import com.redis.util.pool.JedisObjectPoolConfig;
import com.redis.util.pool.WJedisPool;
import redis.clients.jedis.Jedis;

public class Test {
    public static void main(String[] args) throws Exception {
        JedisObjectPoolConfig objectPoolConfig = new JedisObjectPoolConfig();
        objectPoolConfig.setMaxWaitMillis(-1);
        WJedisPool jedisPool = new WJedisPool(objectPoolConfig, "127.0.0.1");
        Jedis jedis = jedisPool.getResource();
        jedis.set("king-001", "king-001");
        System.out.println("main end ...");
    }
}
