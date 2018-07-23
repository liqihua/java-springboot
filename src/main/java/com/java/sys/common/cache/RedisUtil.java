package com.java.sys.common.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * key操作，设置某个key的存活时间
	 * @param key
	 * @param second
	 * @return
	 */
	public boolean expire(String key,int second){
		return redisTemplate.expire(key,second,TimeUnit.SECONDS);
	}


	/**
	 * 方法名：set
	 * 详述：value写操作，永久存活
	 * @param key
	 * @param obj
	 */
	public void set(String key,Object obj) {
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		ops.set(key, obj);
	}
	
	/**
	 * 方法名：set
	 * 详述：value写操作
	 * @param key
	 * @param obj
	 * @param second 存活秒数
	 */
	public void set(String key,Object obj,int second) {
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		ops.set(key, obj,second,TimeUnit.SECONDS);
	}
	
	/**
	 * 方法名：get
	 * 详述：value读操作
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		return ops.get(key);
	}
	
	/**
	 * 方法名：del
	 * 详述：value删除操作
	 * @param key
	 */
	public void del(String key) {
		redisTemplate.delete(key);
	}
	
	/**
	 * 方法名：hasKey
	 * 详述：判断key是否存在
	 * @param key
	 * @return
	 */
	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}
	
	////////////////////////////////////////// value操作 - end ////////////////////////////////////////////////////////////
	
	/**
	 * 方法名：hset
	 * 详述：hash写操作
	 * @param mapName
	 * @param key
	 * @param obj
	 */
	public void hset(String mapName,String key,Object obj) {
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		ops.put(mapName, key, obj);
	}
	
	/**
	 * 方法名：hget
	 * 详述：hash读操作
	 * @param mapName
	 * @param key
	 * @return
	 */
	public Object hget(String mapName,String key) {
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		return ops.get(mapName, key);
	}
	
	/**
	 * 方法名：hHasKey
	 * 详述：判断hash的某个map是否存在某个key
	 * @param mapName
	 * @param key
	 * @return
	 */
	public boolean hHasKey(String mapName,String key) {
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		return ops.hasKey(mapName, key);
	}
	
	/**
	 * 方法名：hdel
	 * 详述：hash删除操作
	 * @param mapName
	 * @param key
	 */
	public void hdel(String mapName,String key) {
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		ops.delete(mapName, key);
	}

	/**
	 * 获取Map
	 * @param mapName
	 */
	public Map<String,Object> entries(String mapName) {
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		return ops.entries(mapName);
	}

	/**
	 * 返回Map的所有key
	 * @param mapName
	 * @return
	 */
	public Set<String> keys(String mapName){
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		return ops.keys(mapName);
	}

	/**
	 * 返回Map的所有value
	 * @param mapName
	 * @return
	 */
	public List<Object> values(String mapName){
		HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
		return ops.values(mapName);
	}

	
	
	////////////////////////////////////////// hash操作 - end ////////////////////////////////////////////////////////////

	/**
	 * set写操作
	 * @param setName
	 * @param obj
	 */
	public void sadd(String setName,Object obj) {
		SetOperations<String,Object> ops = redisTemplate.opsForSet();
		ops.add(setName,obj);
	}

	/**
	 * set删除操作
	 * @param setName
	 * @param obj
	 */
	public void srem(String setName,Object obj){
		SetOperations<String,Object> ops = redisTemplate.opsForSet();
		ops.remove(setName,obj);
	}


	/**
	 * set读操作
	 * @param setName
	 * @return
	 */
	public Set<Object> smembers(String setName){
		SetOperations<String,Object> ops = redisTemplate.opsForSet();
		return ops.members(setName);
	}


	////////////////////////////////////////// set操作 - start ////////////////////////////////////////////////////////////
}
