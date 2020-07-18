package sskf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisService {
    @Autowired
    private RedisTemplate template;

    public void put(String key, Object value, long timeOut) {
        template.opsForValue().set(key, value);
        template.expire(key, timeOut, TimeUnit.MINUTES);
    }

    public Object get(String key) {
       return template.opsForValue().get(key);
    }

    public void delete (String key) {
        template.delete(key);
    }

    public void setTimeOut(String key, long timeOut) {
        template.expire(key, timeOut, TimeUnit.MINUTES);
    }

}
