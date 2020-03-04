package com.study.wang.tenement.util.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


/**
 *  Redis工具类
 */
public class RedisUtil {
    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    /**
     * 默认过期时间,单位/秒, 60*60*2=2H, 两小时
     */
    private static final int DEFAULT_EXPIRE_TIME = 7200;
    private static List<Redis> redisList;

    public static void init(List<Redis> redisList) {
        RedisUtil.redisList = redisList;
    }

    // ------------------------ ShardedJedisPool ------------------------
    private static ShardedJedisPool shardedJedisPool;
    private static ReentrantLock INSTANCE_INIT_LOCL = new ReentrantLock(false);

    /**
     * 获取ShardedJedis实例
     *
     * @return
     */
    private static int timeout = 2;

    private static ShardedJedis getInstance() {
        if (shardedJedisPool == null) {
            try {
                if (INSTANCE_INIT_LOCL.tryLock(timeout, TimeUnit.SECONDS)) {
                    try {
                        if (shardedJedisPool == null) {
                            // JedisPoolConfig
                            JedisPoolConfig config = new JedisPoolConfig();
                            // 最大连接数, 默认8个
                            config.setMaxTotal(200);
                            // 最大空闲连接数, 默认8个
                            config.setMaxIdle(50);
                            // 设置最小空闲数
                            config.setMinIdle(8);
                            // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
                            config.setMaxWaitMillis(10000);
                            // 在获取连接的时候检查有效性, 默认false
                            config.setTestOnBorrow(true);
                            // 调用returnObject方法时，是否进行有效检查
                            config.setTestOnReturn(true);
                            // Idle时进行连接扫描
                            config.setTestWhileIdle(true);
                            //表示idle object evitor两次扫描之间要sleep的毫秒数
                            config.setTimeBetweenEvictionRunsMillis(30000);
                            //表示idle object evitor每次扫描的最多的对象数
                            config.setNumTestsPerEvictionRun(10);
                            //表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
                            config.setMinEvictableIdleTimeMillis(60000);

                            // JedisShardInfo List
                            List<JedisShardInfo> jedisShardInfos = new LinkedList<JedisShardInfo>();
                            for (Redis redis : redisList) {
                                String[] addressInfo = redis.getAddress().split(":");
                                String host = addressInfo[0];
                                int port = Integer.valueOf(addressInfo[1]);
                                JedisShardInfo jedisShardInfo = new JedisShardInfo(host, port, 10000);
                                if (redis.getPassword() != null && !"".equals(redis.getPassword())) {
                                    jedisShardInfo.setPassword(redis.getPassword());
                                }
                                jedisShardInfos.add(jedisShardInfo);
                            }
                            shardedJedisPool = new ShardedJedisPool(config, jedisShardInfos);
                            logger.info(">>>>>>>>>>> JedisUtil.ShardedJedisPool init success.");
                        }

                    } finally {
                        INSTANCE_INIT_LOCL.unlock();
                    }
                }

            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (shardedJedisPool == null) {
            throw new NullPointerException(">>>>>>>>>>> JedisUtil.ShardedJedisPool is null.");
        }

        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        return shardedJedis;
    }

    // ------------------------ serialize and unserialize ------------------------

    /**
     * 将对象-->byte[] (由于jedis中不支持直接存储object所以转换成byte[]存入)
     *
     * @param object
     * @return
     */
    private static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            try {
                oos.close();
                baos.close();
            } catch (IOException e) {
                logger.error("{}", e);
            }
        }
        return null;
    }

    /**
     * 将byte[] -->Object
     *
     * @param bytes
     * @return
     */
    private static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            try {
                bais.close();
            } catch (IOException e) {
                logger.error("{}", e);
            }
        }
        return null;
    }

    // ------------------------ jedis util ------------------------

    /**
     * Set String
     *
     * @param key
     * @param value
     * @param seconds 存活时间,单位/秒
     * @return
     */
    public static String setStringValue(String key, String value, int seconds) {
        String result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.setex(key, seconds, value);
        } catch (Exception e) {
            logger.info("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    /**
     * Set String (默认存活时间, 2H)
     *
     * @param key
     * @param value
     * @return
     */
    public static String setStringValue(String key, String value) {
        return setStringValue(key, value, DEFAULT_EXPIRE_TIME);
    }

    /**
     * Set Object
     *
     * @param key
     * @param obj
     * @param seconds 存活时间,单位/秒
     */
    public static String setObjectValue(String key, Object obj, int seconds) {
        String result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.setex(key.getBytes(), seconds, serialize(obj));
        } catch (Exception e) {
            logger.info("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    /**
     * Set Object (默认存活时间, 2H)
     *
     * @param key
     * @param obj
     * @return
     */
    public static String setObjectValue(String key, Object obj) {
        return setObjectValue(key, obj, DEFAULT_EXPIRE_TIME);
    }

    /**
     * Get String
     *
     * @param key
     * @return
     */
    public static String getStringValue(String key) {
        String value = null;
        ShardedJedis client = getInstance();
        try {
            value = client.get(key);
        } catch (Exception e) {
            logger.info("", e);
        } finally {
            client.close();
        }
        return value;
    }

    /**
     * Get Object
     *
     * @param key
     * @return
     */
    public static Object getObjectValue(String key) {
        Object obj = null;
        ShardedJedis client = getInstance();
        int index = key.indexOf("forever-");
        try {
            byte[] bytes = client.get(key.getBytes());
            if (bytes != null && bytes.length > 0) {
                obj = unserialize(bytes);
            }
        } catch (Exception e) {
            logger.info("", e);
        } finally {
            client.close();
        }
        //重置过期时间
        if(index <0){
            expire(key, DEFAULT_EXPIRE_TIME);
        }
        return obj;
    }

    /**
     * Delete
     *
     * @param key
     * @return Integer reply, specifically:
     * an integer greater than 0 if one or more keys were removed
     * 0 if none of the specified key existed
     */
    public static Long del(String key) {
        Long result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.del(key);
        } catch (Exception e) {
            logger.info("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    /**
     * incrBy	value值加i
     *
     * @param key
     * @param i
     * @return new value after incr
     */
    public static Long incrBy(String key, int i) {
        Long result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.incrBy(key, i);
        } catch (Exception e) {
            logger.info("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    /**
     * exists
     *
     * @param key
     * @return Boolean reply, true if the key exists, otherwise false
     */
    public static Boolean exists(String key) {
        Boolean result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.exists(key);
        } catch (Exception e) {
            logger.info("{}", e);
        } finally {
            client.close();
        }
        return result;
    }

    /**
     * expire	重置存活时间
     *
     * @param key
     * @param seconds 存活时间,单位/秒
     * @return Integer reply, specifically:
     * 1: the timeout was set.
     * 0: the timeout was not set since the key already has an associated timeout (versions lt 2.1.3), or the key does not exist.
     */
    public static Long expire(String key, int seconds) {
        Long result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.expire(key, seconds);
        } catch (Exception e) {
            logger.info("{}", e);
        } finally {
            client.close();
        }
        return result;
    }
    /**
     * expireAt		设置存活截止时间
     *
     * @param key
     * @param unixTime 存活截止时间戳
     * @return
     */
    public static Long expireAt(String key, long unixTime) {
        Long result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.expireAt(key, unixTime);
        } catch (Exception e) {
            logger.info("{}", e);
        } finally {
            client.close();
        }
        return result;
    }


    /**
     * Set Object(永久有效)
     *
     * @param key
     * @param obj
     */
    public static String setForeverObjectValue(String key, Object obj) {
        String result = null;
        ShardedJedis client = getInstance();
        try {
            result = client.set(key.getBytes(),serialize(obj));
        } catch (Exception e) {
            logger.info("{}", e);
        } finally {
            client.close();
        }
        return result;
    }
}
