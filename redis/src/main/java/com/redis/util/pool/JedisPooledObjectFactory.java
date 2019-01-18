package com.redis.util.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import redis.clients.jedis.Jedis;

public class JedisPooledObjectFactory implements PooledObjectFactory<Jedis> {
    private RedisHostAndPort hostAndPort;
    private String password;
    private Integer db;
    private String clientName;

    @Override
    public PooledObject<Jedis> makeObject() {
        Jedis jedis = new Jedis(hostAndPort.getHost(), hostAndPort.getPort());
        if (null != password) {
            jedis.auth(password);
        }
        if (null != db) {
            jedis.select(db);
        }
        if (null != clientName) {
            jedis.clientSetname(clientName);
        }
        return new DefaultPooledObject<>(jedis);
    }

    @Override
    public void destroyObject(PooledObject<Jedis> pooledObject) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject<Jedis> pooledObject) {
        return false;
    }

    @Override
    public void activateObject(PooledObject<Jedis> pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<Jedis> pooledObject) throws Exception {

    }
}
