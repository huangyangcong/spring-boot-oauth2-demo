package com.luangeng.oauthserver.watchmen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis
 */
@Repository
public class RedisDao {

    private static final String KEY = "Watchmen";

    private static final String SEP = ":";

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void log(RequestDesc desc, Watchmen limit) {
        if (limit == null || desc == null) {
            //log
            return;
        }
        String key = makeKey(desc, limit) + desc.getTime();
        redisTemplate.opsForValue().set(key, null, limit.time(), TimeUnit.MILLISECONDS);
    }

    public int count(String key) {
        Set<String> keys = redisTemplate.keys(key + "*");
        return keys.size();
    }

    private String makeKey(RequestDesc desc, Watchmen limit) {
        StringBuilder sb = new StringBuilder(KEY + SEP);
        sb.append(desc.getUrl());
        sb.append(desc.getMethod());
        sb.append(SEP);
        sb.append(limit.toString());
        for (WatchStrategy e : limit.strage()) {
            sb.append(SEP);
            sb.append(e.getvalue(desc));
        }

        return sb.toString();
    }

}
