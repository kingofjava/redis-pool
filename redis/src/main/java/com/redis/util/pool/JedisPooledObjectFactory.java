package com.redis.util.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;

public class JedisPooledObjectFactory implements PooledObjectFactory<Jedis> {
    private RedisHostAndPort hostAndPort;
    private String password;
    private Integer db;
    private String clientName;

    public JedisPooledObjectFactory(String host, int port) {
        this(host, port, 0, null, null);
    }

    public JedisPooledObjectFactory(String host, int port, Integer db, String password, String clientName) {
        this.hostAndPort = new RedisHostAndPort(host, port);
        this.db = db;
        this.password = password;
        this.clientName = clientName;
    }

    @Override
    public PooledObject<Jedis> makeObject() {
        WJedis jedis = new WJedis(hostAndPort.getHost(), hostAndPort.getPort());
        if (null != password) {
            jedis.auth(password);
        }
        if (null != db && db.intValue() != 0) {
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
        BinaryJedis jedis = pooledObject.getObject();
        if (jedis.getDB().intValue() != this.db) {
            jedis.select(this.db);
        }
    }

    @Override
    public void passivateObject(PooledObject<Jedis> pooledObject) throws Exception {

    }
}
