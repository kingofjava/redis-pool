package com.redis.util;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JedisConfig {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        Map<String,String> map = new HashMap<>();
        map.put("13789039820","13789039820");
        map.put("13789039821","13789039820");
        map.put("13789039822","13789039820");
        map.put("13789039823","13789039820");
        Set<Map.Entry<String,String>> set=map.entrySet();
        Iterator<Map.Entry<String,String>> iterator=set.iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry=iterator.next();
            jedis.hset("telephone",entry.getKey(),entry.getValue());
        }
        System.out.println(jedis.hget("telephone","13789039823"));
    }
}