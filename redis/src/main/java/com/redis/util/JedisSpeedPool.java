package com.redis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisSpeedPool {
    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(1000);
        config.setMinIdle(500);
        config.setMaxWaitMillis(3000);
        JedisPool jp = new JedisPool(config,"127.0.0.1", 6379);
        Jedis jedis = jp.getResource();
        System.out.println(jedis.get("name"));
    }
}
