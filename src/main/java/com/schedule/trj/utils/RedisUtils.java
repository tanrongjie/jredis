package com.schedule.trj.utils;


import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @BelongsProject: redis
 * @BelongsPackage: com.redis.trj.utils
 * @Author: 谭荣杰
 * @CreateTime: 2018-11-21 20:51
 * @Description:
 */
@Component
public class RedisUtils {
    private final JedisPool jedisPool;

    public RedisUtils(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private void colse(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            boolean flag = jedis.exists(key);
            colse(jedis);
            return flag;
        } catch (Exception e) {
            colse(jedis);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        Jedis jedis = jedisPool.getResource();
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                jedis.del(key[0]);
            } else {
                jedis.del(key);
            }
        }
        colse(jedis);
    }

    /**
     * 设置key生存时间，当key过期时，它会被自动删除。
     *
     * @param key  键值
     * @param time 单位:秒
     */
    public void expire(String key, int time) {
        Jedis jedis = jedisPool.getResource();
        jedis.expire(key, time);
        colse(jedis);
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        if (null == key) {
            return null;
        }
        Jedis jedis = jedisPool.getResource();
        Object value = jedis.get(key);
        colse(jedis);
        return value;
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        Jedis jedis = jedisPool.getResource();
        if (null == key || "".equals(key) || null == value) {
            return false;
        }
        try {
            jedis.set(key, value.toString());
            colse(jedis);
            return true;
        } catch (Exception e) {
            colse(jedis);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, int time) {
        Jedis jedis = jedisPool.getResource();
        if (null == key || "".equals(key) || null == value) {
            return false;
        }
        try {
            if (time > 0) {
                jedis.setex(key, time, value.toString());
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            colse(jedis);
            return false;
        }
    }

    /**
     * 清空redis所有记录
     *
     * @return
     */
    public String emptyAll() {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.flushAll();
        colse(jedis);
        return result;
    }

    /**
     * 获取redis 中所有key的个数
     *
     * @return
     */
    public Long getSize() {
        Jedis jedis = jedisPool.getResource();
        long size = jedis.dbSize();
        colse(jedis);
        return size;
    }


    /**
     * 获取所有key
     *
     * @return
     */
    public Set<String> getAllKey() {
        Jedis jedis = jedisPool.getResource();
        return jedis.keys("*");
    }

    /**
     * 添加Map
     *
     * @param map
     * @return
     */
    public String setMap(Map<String, String> map) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hmset("map", map);
        jedis.close();
        return result;
    }

    /**
     * 添加一个list数据, 向左插入一个元素
     *
     * @param key
     * @param value
     * @return
     */
    public Long lpush(String key, String... values) {
        if (null != key && !"".equals(key)) {
            Jedis jedis = jedisPool.getResource();
            Long length = jedis.lpush(key, values);
            jedis.close();
            return length;
        }
        return 0L;
    }

    /**
     * 返回List长度
     *
     * @param key
     * @return
     */
    public Long llen(String key) {
        if (null != key && !"".equals(key)) {
            Jedis jedis = jedisPool.getResource();
            Long length = jedis.llen(key);
            jedis.close();
            return length;
        }
        return 0L;
    }

    /**
     * 获取list集合区间
     *
     * @param key   键
     * @param start list下标开始位置
     * @param end   list下标结束位置
     * @return
     */
    public List<String> lrange(String key, Integer start, Integer end) {
        if (null != key && !"".equals(key)) {
            Jedis jedis = jedisPool.getResource();
            List<String> list = jedis.lrange(key, start, end);
            jedis.close();
            return list;
        }
        return null;
    }

    /**
     * 删除指定list集合区间之外的数据,列如传递的数据
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return
     */
    public String ltrim(String key, Long start, Long end) {
        if (null != key && !"".equals(key)) {
            Jedis jedis = jedisPool.getResource();
            String reslut = jedis.ltrim(key, start, end);
            jedis.close();
            return reslut;
        }
        return "";
    }

    /**
     * 向左出栈一个元素
     *
     * @param key
     * @return
     */
    public String lpop(String key) {
        Jedis jedis = jedisPool.getResource();
        String reslut = jedis.lpop(key);
        jedis.close();
        return reslut;
    }

    /**
     * 判断值是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        Jedis jedis = jedisPool.getResource();
        boolean flag = jedis.exists(key);
        jedis.close();
        return flag;
    }
}
