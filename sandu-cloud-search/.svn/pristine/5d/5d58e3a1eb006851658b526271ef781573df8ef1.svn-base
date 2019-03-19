package com.sandu.search.service.redis.impl;

import com.sandu.search.common.tools.JsonUtil;
import com.sandu.search.common.tools.ShardedNormalJedisPool;
import com.sandu.search.common.util.SerializeUtil;
import com.sandu.search.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;

/**
 * @desc Redis缓存服务
 * @date 20171017
 * @auth pengxuangang
 */
@Slf4j
@Service("redisService")
public class RedisServiceImpl implements RedisService {

    private final static String CLASS_LOG_PREFIX = "[Redis缓存服务]:";

    private final ShardedNormalJedisPool shardedJedisPool;

    @Autowired
    public RedisServiceImpl(ShardedNormalJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    /**
     * 设置一个key的过期时间（单位：秒）
     *
     * @param key     key值
     * @param seconds 多少秒后过期
     * @return 1：设置了过期时间 0：没有设置过期时间/不能设置过期时间
     */
    @Override
    public long expire(String key, int seconds) {
        if (key == null || "".equals(key)) {
            return 0;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.expire(key, seconds);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "EXPIRE error[key=" + key + " seconds=" + seconds + "]" + ex.getMessage(), ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return 0;
    }

    /**
     * 设置一个key在某个时间点过期
     *
     * @param key           key值
     * @param unixTimestamp unix时间戳，从1970-01-01 00:00:00开始到现在的秒数
     * @return 1：设置了过期时间 0：没有设置过期时间/不能设置过期时间
     */
    @Override
    public long expireAt(String key, long unixTimestamp) {
        if (key == null || "".equals(key)) {
            return 0;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.expireAt(key, unixTimestamp);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "EXPIRE error[key=" + key + " unixTimestamp=" + unixTimestamp + "]" + ex.getMessage(), ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return 0;
    }

    /**
     * 截断一个List
     *
     * @param key   列表key
     * @param start 开始位置 从0开始
     * @param end   结束位置
     * @return 状态码
     */
    @Override
    public String trimList(String key, long start, long end) {
        if (key == null || "".equals(key)) {
            return "-";
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.ltrim(key, start, end);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "LTRIM 出错[key=" + key + " start=" + start + " end=" + end + "]" + ex.getMessage(), ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return "-";
    }

    /**
     * 检查Set长度
     *
     * @param key
     * @return
     */
    @Override
    public long countSet(String key) {
        if (key == null) {
            return 0;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.scard(key);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "countSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return 0;
    }

    /**
     * 添加到Set中（同时设置过期时间）
     *
     * @param key     key值
     * @param seconds 过期时间 单位s
     * @param value
     * @return 成功true
     */
    @Override
    public boolean addSet(String key, int seconds, String... value) {
        boolean result = addSet(key, value);
        if (result) {
            long i = expire(key, seconds);
            return i == 1;
        }
        return false;
    }

    /**
     * 添加到Set中
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean addSet(String key, String... value) {
        if (key == null || value == null) {
            return false;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.sadd(key, value);
            return true;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "setList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * @param key
     * @param value
     * @return 判断值是否包含在set中
     */
    @Override
    public boolean containsInSet(String key, String value) {
        if (key == null || value == null) {
            return false;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.sismember(key, value);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "setList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * 获取Set
     *
     * @param key
     * @return
     */
    @Override
    public Set<String> getSet(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.smembers(key);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "getList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * 从set中删除value
     *
     * @param key
     * @return
     */
    @Override
    public boolean removeSetValue(String key, String... value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.srem(key, value);
            return true;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "getList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }


    /**
     * 添加到Map中
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean addMap(String mapName, String key, String value) {
        if (null == mapName || key == null || value == null) {
            return false;
        }

        Map<String, String> objectMap = new HashMap<>(1);
        objectMap.put(key, value);

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.hmset(mapName, objectMap);
            return true;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "setMap error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * 添加到Map中
     *
     * @param mapName   Map名称
     * @param objectMap Map对象
     * @return
     */
    @Override
    public boolean addMap(String mapName, Map<String, String> objectMap) {
        if (null == mapName || null == objectMap) {
            return false;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.hmset(mapName, objectMap);
            return true;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "setMap error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * 添加到Map中(长度兼容方法)
     *
     * @param mapName   Map名称
     * @param objectMap Map对象
     * @return
     */
    @Override
    public boolean addMapCompatible(String mapName, Map<String, String> objectMap) {
        if (null == mapName || null == objectMap) {
            return false;
        }

        //单次数据提交长度
        int maxLength = 3000;

        if (maxLength < objectMap.size()) {
            log.info(CLASS_LOG_PREFIX + "当前Map超长，分段提交,当前长度:{}.", objectMap.size());
        }

        int initCount = 0;
        Map<String, String> newMap = new HashMap<>();
        for (Map.Entry<String, String> entries : objectMap.entrySet()) {
            newMap.put(entries.getKey(), entries.getValue());

            if (maxLength == newMap.size()) {
                //移除空值
                newMap = removeNullValueFromMap(newMap);
                //提交
                log.info(CLASS_LOG_PREFIX + "正在提交{}-{}条.", initCount, initCount + maxLength);
                /*if(initCount % 50000 == 0) {
                	log.info(CLASS_LOG_PREFIX + "已经提交条.", initCount);
                }*/
                initCount = initCount + maxLength;
                //提交数据
                try {
                    addMap(mapName, newMap);
                } catch (Exception e) {
                    log.error(CLASS_LOG_PREFIX + "分段提交失败:{}.", e);
                    return false;
                }
                //清空待提交列表
                newMap.clear();
            }
        }

        //不足最大长度部分再次提交
        if (0 < newMap.size()) {
            //移除空值
            newMap = removeNullValueFromMap(newMap);
            log.info(CLASS_LOG_PREFIX + "正在提交{}-{}条.", initCount, initCount + newMap.size());
            //提交数据
            try {
                addMap(mapName, newMap);
            } catch (Exception e) {
                log.error(CLASS_LOG_PREFIX + "分段提交失败:{}.", e);
                return false;
            }
        }

        return true;
    }

    /**
     * 获取Map数据
     *
     * @param mapName Map名称
     * @param key     Map的Key对象
     * @return
     */
    @Override
    public String getMap(String mapName, String key) {
        if (null == mapName || null == key) {
            return null;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hget(mapName, key);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "getMap error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * 获取Map多个数据
     *
     * @param mapName     Map名称
     * @param keyList     Map的Key对象
     * @return
     */
    @Override
    public Map<String, String> getMap(String mapName, List<String> keyList) {
        if (null == mapName || null == keyList || 0 >= keyList.size()) {
            return null;
        }

        Map<String, String> map = new HashMap<>(keyList.size());

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            for (String key : keyList) {
                String value = shardedJedis.hget(mapName, key);
                if (!StringUtils.isEmpty(value) && !"-1".equals(key)) {
                    map.put(key, value);
                }
            }
            return map;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "getMap error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * 获取Map所有数据
     *
     * @param mapName Map名称
     * @return
     */
    @Override
    public Map<String, String> getMap(String mapName) {
        if (null == mapName) {
            return null;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hgetAll(mapName);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "getMapAll error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * 删除Map数据
     *
     * @param mapName Map名称
     * @param key     Map的Key对象
     * @return
     */
    @Override
    public boolean delMap(String mapName, String key) {
        if (null == mapName || null == key) {
            return false;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.hdel(mapName, key);
            return true;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "delMap error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * 从list中删除value 默认count 1
     *
     * @param key
     * @param values 值list
     * @return
     */
    @Override
    public int removeListValue(String key, List<String> values) {
        return removeListValue(key, 1, values);
    }

    /**
     * 从list中删除value
     *
     * @param key
     * @param count
     * @param values 值list
     * @return
     */
    @Override
    public int removeListValue(String key, long count, List<String> values) {
        int result = 0;
        if (values != null && values.size() > 0) {
            for (String value : values) {
                if (removeListValue(key, count, value)) {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * 从list中删除value
     *
     * @param key
     * @param count 要删除个数
     * @param value
     * @return
     */
    @Override
    public boolean removeListValue(String key, long count, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.lrem(key, count, value);
            return true;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "getList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * 截取List
     *
     * @param key
     * @param start 起始位置
     * @param end   结束位置
     * @return
     */
    @Override
    public List<String> rangeList(String key, long start, long end) {
        if (key == null || "".equals(key)) {
            return null;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.lrange(key, start, end);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "rangeList 出错[key=" + key + " start=" + start + " end=" + end + "]" + ex.getMessage(), ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * 检查List长度
     *
     * @param key
     * @return
     */
    @Override
    public long countList(String key) {
        if (key == null) {
            return 0;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.llen(key);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "countList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return 0;
    }

    /**
     * 添加到List中（同时设置过期时间）
     *
     * @param key     key值
     * @param seconds 过期时间 单位s
     * @param value
     * @return
     */
    @Override
    public boolean addList(String key, int seconds, String... value) {
        boolean result = addList(key, value);
        if (result) {
            long i = expire(key, seconds);
            return i == 1;
        }
        return false;
    }

    /**
     * 添加到List
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean addList(String key, String... value) {
        if (key == null || value == null) {
            return false;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.lpush(key, value);
            return true;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "setList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * 添加到List(只新增)
     *
     * @param key
     * @return
     */
    @Override
    public boolean addList(String key, List<String> list) {
        if (key == null || list == null || list.size() == 0) {
            return false;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.lpush(key, list.toArray(new String[list.size()]));
            return true;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "setList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return true;
    }

    /**
     * 获取List
     *
     * @param key
     * @return
     */
    @Override
    public List<String> getList(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.lrange(key, 0, -1);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "getList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * 设置HashSet对象
     *
     * @param domain 域名
     * @param key    键值
     * @param value  Json String or String value
     * @return
     */
    @Override
    public boolean setHSet(String domain, String key, String value) {
        if (value == null) {
            return false;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.hset(domain, key, value);
            return true;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "setHSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * 获得HashSet对象
     *
     * @param domain 域名
     * @param key    键值
     * @return Json String or String value
     */
    @Override
    public String getHSet(String domain, String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hget(domain, key);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "getHSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * 删除HashSet对象
     *
     * @param domain 域名
     * @param key    键值
     * @return 删除的记录数
     */
    @Override
    public long delHSet(String domain, String key) {
        ShardedJedis shardedJedis = null;
        long count = 0;
        try {
            shardedJedis = shardedJedisPool.getResource();
            count = shardedJedis.hdel(domain, key);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "delHSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return count;
    }

    /**
     * 删除HashSet对象
     *
     * @param domain 域名
     * @param key    键值
     * @return 删除的记录数
     */
    @Override
    public long delHSet(String domain, String... key) {
        ShardedJedis shardedJedis = null;
        long count = 0;
        try {
            shardedJedis = shardedJedisPool.getResource();
            count = shardedJedis.hdel(domain, key);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "delHSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return count;
    }

    /**
     * 判断key是否存在
     *
     * @param domain 域名
     * @param key    键值
     * @return
     */
    @Override
    public boolean existsHSet(String domain, String key) {
        ShardedJedis shardedJedis = null;
        boolean isExist = false;
        try {
            shardedJedis = shardedJedisPool.getResource();
            isExist = shardedJedis.hexists(domain, key);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "existsHSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return isExist;
    }

    /**
     * 全局扫描hset
     *
     * @param match field匹配模式
     * @return
     */
    @Override
    public List<Map.Entry<String, String>> scanHSet(String domain, String match) {
        ShardedJedis shardedJedis = null;
        try {
            int cursor = 0;
            shardedJedis = shardedJedisPool.getResource();
            ScanParams scanParams = new ScanParams();
            scanParams.match(match);
            Jedis jedis = shardedJedis.getShard(domain);
            ScanResult<Map.Entry<String, String>> scanResult;
            List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>();
            do {
                scanResult = jedis.hscan(domain, String.valueOf(cursor), scanParams);
                list.addAll(scanResult.getResult());
                cursor = Integer.parseInt(scanResult.getStringCursor());
            } while (cursor > 0);
            return list;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "scanHSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * 返回 domain 指定的哈希集中所有字段的value值
     *
     * @param domain
     * @return
     */

    @Override
    public List<String> hvals(String domain) {
        ShardedJedis shardedJedis = null;
        List<String> retList = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            retList = shardedJedis.hvals(domain);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "hvals error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return retList;
    }

    /**
     * 返回 domain 指定的哈希集中所有字段的key值
     *
     * @param domain
     * @return
     */

    @Override
    public Set<String> hkeys(String domain) {
        ShardedJedis shardedJedis = null;
        Set<String> retList = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            retList = shardedJedis.hkeys(domain);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "hkeys error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return retList;
    }

    /**
     * 返回 domain 指定的哈希key值总数
     *
     * @param domain
     * @return
     */
    @Override
    public long lenHset(String domain) {
        ShardedJedis shardedJedis = null;
        long retList = 0;
        try {
            shardedJedis = shardedJedisPool.getResource();
            retList = shardedJedis.hlen(domain);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "hkeys error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return retList;
    }

    /**
     * 设置排序集合
     *
     * @param key
     * @param score
     * @param value
     * @return
     */
    @Override
    public boolean setSortedSet(String key, long score, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.zadd(key, score, value);
            return true;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "setSortedSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * 获得排序集合
     *
     * @param key
     * @param startScore
     * @param endScore
     * @param orderByDesc
     * @return
     */
    @Override
    public Set<String> getSoredSet(String key, long startScore, long endScore, boolean orderByDesc) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (orderByDesc) {
                return shardedJedis.zrevrangeByScore(key, endScore, startScore);
            } else {
                return shardedJedis.zrangeByScore(key, startScore, endScore);
            }
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "getSoredSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * 计算排序长度
     *
     * @param key
     * @param startScore
     * @param endScore
     * @return
     */
    @Override
    public long countSoredSet(String key, long startScore, long endScore) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            Long count = shardedJedis.zcount(key, startScore, endScore);
            return count == null ? 0L : count;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "countSoredSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return 0L;
    }

    /**
     * 删除排序集合
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean delSortedSet(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            long count = shardedJedis.zrem(key, value);
            return count > 0;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "delSortedSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * 获得排序集合
     *
     * @param key
     * @param startRange
     * @param endRange
     * @param orderByDesc
     * @return
     */
    @Override
    public Set<String> getSoredSetByRange(String key, int startRange, int endRange, boolean orderByDesc) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (orderByDesc) {
                return shardedJedis.zrevrange(key, startRange, endRange);
            } else {
                return shardedJedis.zrange(key, startRange, endRange);
            }
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "getSoredSetByRange error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * 获得排序打分
     *
     * @param key
     * @return
     */
    @Override
    public Double getScore(String key, String member) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.zscore(key, member);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "getSoredSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    @Override
    public boolean set(String key, String value, int second) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.setex(key, second, value);
            return true;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "set error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    @Override
    public boolean set(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.set(key, value);
            return true;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "set error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    @Override
    public Object getValue(String key) {
        Object ret = null;
        ShardedJedis jedis = shardedJedisPool.getResource();
        try {

            // 去redis中取回序列化后的对象
            byte[] obj = jedis.get(key.getBytes("UTF-8"));

            // 取回的对象反序列化
            if (obj != null) {
                ret = SerializeUtil.unserialize(obj);
            }

        } catch (Exception e) {
            jedis.close();
        } finally {
            jedis.close();
        }
        return ret;
    }

    @Override
    public String get(String key, String defaultValue) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.get(key) == null ? defaultValue : shardedJedis.get(key);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "get error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return defaultValue;
    }
    
    @Override
    public String get(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.get(key);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "get error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
       return null;
    }

    @Override
    public boolean del(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.del(key);
            return true;
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "del error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    @Override
    public long incr(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.incr(key);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "incr error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return 0;
    }

    @Override
    public long decr(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.decr(key);
        } catch (Exception ex) {
            log.error(CLASS_LOG_PREFIX + "incr error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return 0;
    }

    private void returnBrokenResource(ShardedJedis shardedJedis) {
        if (shardedJedis == null) {
            return;
        }
        try {
            // 容错
            shardedJedis.close();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "returnBrokenResource error.", e);
        }
    }

    private void returnResource(ShardedJedis shardedJedis) {
        try {
            shardedJedis.close();
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "returnResource error.", e);
        } finally {
            //打印资源情况
            if (0 < shardedJedisPool.getNumActive()) {
                log.error(CLASS_LOG_PREFIX + "连接池溢出，该链接未被关闭，正在尝试重新关闭...");
                try {
                    shardedJedis.close();
                } catch (Exception e) {
                    log.error(CLASS_LOG_PREFIX + "重试关闭资源异常! error.", e);
                    shardedJedis.disconnect();
                } finally {
                    log.error(CLASS_LOG_PREFIX + "已重试关闭，当前已持有资源数:{}", shardedJedisPool.getNumActive());
                }

            }
        }
    }

    @Override
    public Long getSizeOfList(String key) {
        ShardedJedis jedis = null;
        Long size = 0L;
        try {
            jedis = getResource();
            size = jedis.llen(key);
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "getSizeOfList {} = {}", key, size);
        } finally {
            returnResource(jedis);
        }
        return size;
    }

    /**
     * 获取资源
     *
     * @return
     * @throws JedisException
     */
    public ShardedJedis getResource() throws JedisException {
        ShardedJedis jedis = null;
        try {
            jedis = shardedJedisPool.getResource();
        } catch (JedisException e) {
            log.error(CLASS_LOG_PREFIX + "getResource.", e);
        }
        return jedis;
    }

    /**
     * 获取List缓存,出队列(阻塞模式)
     *
     * @param key 键
     * @return 值
     */
    @Override
    public List<String> getBrpopList(String key) {
        List<String> value = null;
        ShardedJedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.blpop(2, key);
                Long size = jedis.llen(key);
                log.error(CLASS_LOG_PREFIX + "getBrpopList size= " + size);
                log.info(CLASS_LOG_PREFIX + "getObjectList {} = {}", key, value);
            }
        } catch (Exception e) {
            log.error(CLASS_LOG_PREFIX + "getObjectList {} = {}, Exception:{}.", new String[]{
                key, JsonUtil.toJson(value), e.toString()
            });
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 向List缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    @Override
    public long listLAdd(String key, String... value) {
        long result = 0;
        ShardedJedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.lpush(key, value);
            log.debug("listAdd {} = {}", key, value);
        } catch (Exception e) {
            log.warn(CLASS_LOG_PREFIX + "listAdd {} = {}", new String[]{
                    key, JsonUtil.toJson(value), e.toString()
            });
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 从Map中移除空值
     *
     * @param map
     * @return
     */
    private static Map<String, String> removeNullValueFromMap(Map<String, String> map) {

        if (null == map || 0 >= map.size()) {
            return null;
        }

        //待移除Map键
        Set<String> waitRemoveKey = new HashSet<>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            if (StringUtils.isEmpty(entry.getValue()) || StringUtils.isEmpty(key)) {
                waitRemoveKey.add(key);
            }
        }

        //移除
        if (0 < waitRemoveKey.size()) {
            Iterator<String> iterator = waitRemoveKey.iterator();
            while (iterator.hasNext()) {
                String nextKey = iterator.next();
                map.remove(nextKey);
            }
        }

        return map;
    }
}
