package com.sandu.search.service.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存服务
 *
 * @date 2018/3/13
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
public interface RedisService {

    long expire(String key, int seconds);

    long expireAt(String key, long unixTimestamp);

    String trimList(String key, long start, long end);

    long countSet(String key);

    boolean addSet(String key, int seconds, String... value);

    boolean addSet(String key, String... value);

    boolean containsInSet(String key, String value);

    Set<String> getSet(String key);

    boolean removeSetValue(String key, String... value);

    boolean addMap(String mapName, String key, String value);

    boolean addMap(String mapName, Map<String, String> objectMap);

    boolean addMapCompatible(String mapName, Map<String, String> objectMap);

    String getMap(String mapName, String key);

    Map<String, String> getMap(String mapName, List<String> keyList);

    boolean delMap(String mapName, String key);

    int removeListValue(String key, List<String> values);

    int removeListValue(String key, long count, List<String> values);

    boolean removeListValue(String key, long count, String value);

    List<String> rangeList(String key, long start, long end);

    long countList(String key);

    boolean addList(String key, int seconds, String... value);

    boolean addList(String key, String... value);

    boolean addList(String key, List<String> list);

    List<String> getList(String key);

    boolean setHSet(String domain, String key, String value);

    String getHSet(String domain, String key);

    long delHSet(String domain, String key);

    long delHSet(String domain, String... key);

    boolean existsHSet(String domain, String key);

    List<Map.Entry<String, String>> scanHSet(String domain, String match);

    List<String> hvals(String domain);

    Set<String> hkeys(String domain);

    long lenHset(String domain);

    boolean setSortedSet(String key, long score, String value);

    Set<String> getSoredSet(String key, long startScore, long endScore, boolean orderByDesc);

    long countSoredSet(String key, long startScore, long endScore);

    boolean delSortedSet(String key, String value);

    Set<String> getSoredSetByRange(String key, int startRange, int endRange, boolean orderByDesc);

    Double getScore(String key, String member);

    boolean set(String key, String value, int second);

    boolean set(String key, String value);

    Object getValue(String key);

    String get(String key, String defaultValue);
    
    String get(String key);

    boolean del(String key);

    long incr(String key);

    long decr(String key);

    Map<String, String> getMap(String mapName);

    Long getSizeOfList(String key);

    /**
     * 获取List缓存,出队列(阻塞模式)
     *
     * @param key 键
     * @return 值
     */
    List<String> getBrpopList(String key);

    /**
     * 向List缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    long listLAdd(String key, String... value);
}
