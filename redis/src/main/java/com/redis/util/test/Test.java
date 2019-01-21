package com.redis.util.test;

import com.redis.util.pool.JedisObjectPoolConfig;
import com.redis.util.pool.WJedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Test {
    public static void main(String[] args) throws Exception {
        JedisPool pool = null;
        JedisObjectPoolConfig objectPoolConfig = new JedisObjectPoolConfig();
        objectPoolConfig.setMaxWaitMillis(-1);
        WJedisPool jedisPool = new WJedisPool(objectPoolConfig, "127.0.0.1");
        Jedis jedis = jedisPool.getResource();
        System.out.println(jedis.ping());
//        jedis.set("king-001", "king-001");
        System.out.println("main end ...");

    }
}
