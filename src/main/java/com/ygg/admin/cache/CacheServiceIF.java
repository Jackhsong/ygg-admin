package com.ygg.admin.cache;

import org.apache.ibatis.cache.CacheException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CacheServiceIF
{
    
    boolean delete(String key)
        throws CacheException;
    
    void flushAll();
    
    Object get(String key);
    
    Integer getInt(String key);
    
    Long getLong(String key);
    
    String getString(String key);
    
    void set(String key, Object value, int expire);
    
    int size();
    
    Set<String> keys();

    public Map<String, Object> mgetstring(List<String> key);

    long incr(String key, long delta);

    /**分布式锁 ttl 锁定时间  */
    boolean acquireLock(String key, int ttl);

    boolean releaseLock(String key);
    
}
