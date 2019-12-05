package com.rabbitmq.utils;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Collections;

/**
 * Created by star on 2019/11/19.
 */
public class RedisLock {



    private static final int expireTime = 30*1000;




    private static JedisPool jedisPool = null;


    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(50);
            config.setMaxIdle(5);
            config.setMaxWaitMillis(200000);
            config.setTestOnBorrow(true);
            jedisPool = new JedisPool(config, "120.76.240.252", 6379, 200000,"123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized static Jedis getResource() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 设置锁
     * @param lockKey
     * @param requestId
     * @return
     */
    public static boolean lock( String lockKey, String requestId) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            String result = jedis.set(lockKey, requestId, "NX", "PX", expireTime);
            if ("OK".equals(result)) {
                return true;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            returnResource(jedis);
        }
        return false;
    }

    /**
     * 释放锁
     * @param lockKey
     * @param requestId
     * @return
     */
    public static boolean unLock( String lockKey, String requestId) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            if (result instanceof  Long && 1 == (Long) result) {
                return true;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            returnResource(jedis);
        }
        return false;
    }




}
