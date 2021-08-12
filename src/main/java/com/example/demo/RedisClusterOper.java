package com.example.demo;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisClusterOper {
	@Autowired
    StringRedisTemplate redisTemplate;
    
    ValueOperations<String, String> stringRedis;
    HashOperations<String, Object, Object> hashRedis;
    ListOperations<String, String> listRedis;
    SetOperations<String, String> setRedis;
    
    @PostConstruct
    public void init(){
        stringRedis=redisTemplate.opsForValue();
        hashRedis = redisTemplate.opsForHash();
        listRedis = redisTemplate.opsForList();
        setRedis = redisTemplate.opsForSet();
    }
    
    public void set(String key,String value)
    {
    	stringRedis.set(key, value);
    }
    
    public void batchset(Map<String, String> key_val)
    {
    	stringRedis.multiSet(key_val);
    }
    
    public void hmset(String key,Map<String, String> key_val)
    {
    	hashRedis.putAll(key, key_val);
    }
    
    public void hset(String key,String field,String val)
    {
    	hashRedis.put(key, field, val);
    }
    
    public void leftPush(String key, List<String> values)
    {
    	listRedis.leftPushAll(key, values);
    }
    
    public void sadd(String key,String ...val)
    {
    	setRedis.add(key, val);
    }
    
    public String get(String key)
    {
    	return stringRedis.get(key);
    }

	public Long increment(String key) {
		return stringRedis.increment(key);
	}

}
