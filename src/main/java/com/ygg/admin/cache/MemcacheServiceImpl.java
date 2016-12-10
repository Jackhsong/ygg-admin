package com.ygg.admin.cache;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import org.apache.ibatis.cache.CacheException;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemcacheServiceImpl implements CacheServiceIF
{
    
    private static Logger log = Logger.getLogger(MemcacheServiceImpl.class);
    
    private MemcachedClient mc = null;
    
    MemcacheServiceImpl(String host, String port)
        throws IOException
    {
        log.info("缓存单例00始化------------------------------");
        mc = new MemcachedClient( 
        		new BinaryConnectionFactory()
        		, AddrUtil.getAddresses("127.0.0.1:11211"));
    }
    
    @Override
    public boolean delete(String key)
        throws CacheException
    {
        try
        {
            return mc.delete(key).get();
        }
        catch (Exception e)
        {
            log.error("delete出错！");
            throw new CacheException(e);
        }
    }
    
    @Override
    public void flushAll()
    {
        mc.flush();
    }
    
    @Override
    public Object get(String key)
    {
        return mc.get(key);
    }
    
    @Override
    public Integer getInt(String key)
    {
        return mc.get(key) != null ? (Integer)mc.get(key) : null;
    }
    
    @Override
    public Long getLong(String key)
    {
        return mc.get(key) != null ? (Long)mc.get(key) : null;
    }
    
    @Override
    public String getString(String key)
    {
        return mc.get(key) != null ? (String)mc.get(key) : null;
    }
    
    @Override
    public void set(String key, Object value, int expire)
    {
        if (value != null)
        {
            mc.set(key, expire, value);
        }
    }
    
    @Override
    public int size()
    {
        throw new UnsupportedOperationException("MemcacheServiceImpl不支持该方法!");
    }
    
    @Override
    public Set<String> keys()
    {
        throw new UnsupportedOperationException("MemcacheServiceImpl不支持该方法!");
    }

    @Override
    public Map<String, Object> mgetstring(List<String> key) {
        try {
            return mc.getBulk(key);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public long incr(String key, long delta) {
        try {
            return mc.incr(key, delta);
        } catch (Exception e) {
            log.error(e);
        }
        return -1L;
    }

    @Override
    public boolean acquireLock(String key, int ttl) {
        try {
            long r = mc.incr(key, 1L, 0L, ttl);
            if (r == 0L) return true;
        } catch (Exception e) {
            log.error("acquireLock_failed:", e);
        }
        return false;
    }

    @Override
    public boolean releaseLock(String key) {
        return delete(key);
    }

}
