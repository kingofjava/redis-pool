package com.redis.util.test;

import com.redis.util.pool.JedisObjectPoolConfig;
import com.redis.util.pool.WJedis;
import com.redis.util.pool.WJedisPool;

public class TestMaxWait {
    public static void main(String[] args) throws Exception {
        JedisObjectPoolConfig objectPoolConfig = new JedisObjectPoolConfig();
        objectPoolConfig.setMaxWaitMillis(8000);
        objectPoolConfig.setMaxTotal(1);
        WJedisPool jedisPool = new WJedisPool(objectPoolConfig, "127.0.0.1");
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        WJedis jedis = jedisPool.getResource();
                        System.out.println(jedis.get("sun"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//        jedis.set("king-001", "king-001");
                }
            }).start();
        }
        System.out.println("main end ...");

    }
}
