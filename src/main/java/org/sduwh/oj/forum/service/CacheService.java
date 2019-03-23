package org.sduwh.oj.forum.service;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.sduwh.oj.forum.common.CacheKeyConstants;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

@Service("cacheService")
@Slf4j
public class CacheService {

    @Resource(name = "redisPool")
    private RedisPool redisPool;

    private Gson gson = new Gson();

    public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix) {
        saveCache(toSavedValue, timeoutSeconds, prefix, null);
    }

    public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix, String... keys) {

        if (toSavedValue == null) {
            return;
        }

        Jedis jedis = null;

        try {
            String cacheKey = generateCacheKey(prefix, keys);
            jedis = redisPool.instance();
            jedis.setex(cacheKey, timeoutSeconds, toSavedValue);
        } catch (Exception e) {
            log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), gson.toJson(keys));
        } finally {
            redisPool.safeClose(jedis);
        }
    }

    public String getFromCache(CacheKeyConstants prefix, String... keys) {

        Jedis shardedJedis = null;
        String cacheKey = generateCacheKey(prefix, keys);

        try {
            shardedJedis = redisPool.instance();
            String value = shardedJedis.get(cacheKey);
            return value;
        } catch (Exception e) {
            log.error("get from cache exception, prefix:{}, keys:{}", prefix.name(), gson.toJson(keys));
            return null;
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    private String generateCacheKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }
}
