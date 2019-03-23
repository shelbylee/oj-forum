package org.sduwh.oj.forum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Service("redisPool")
public class RedisPool {

    public RedisPool() {}

    private static class NestClass {
        private static JedisPool jedisPool;
        static {
            jedisPool = new JedisPool();
        }
    }

    public static Jedis instance() {
        return NestClass.jedisPool.getResource();
    }

    public void safeClose(Jedis jedis) {
        try {
            if (jedis != null) {
                jedis.close();
            }
        } catch (Exception e) {
            log.error("return redis resource exception", e);
        }
    }

}
