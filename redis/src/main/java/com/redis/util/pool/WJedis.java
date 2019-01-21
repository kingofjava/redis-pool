package com.redis.util.pool;

import redis.clients.jedis.Jedis;

public class WJedis extends Jedis {

    public WJedis(String host, int port) {
        super(host, port);
    }

    @Override
    public String get(String key) {
        System.out.println("begin to get value by key ...");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("get value sleep end ...");
        return super.get(key);
    }
}
