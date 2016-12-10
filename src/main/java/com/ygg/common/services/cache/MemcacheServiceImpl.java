package com.ygg.common.services.cache;

import java.io.IOException;
import java.util.Set;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

import org.apache.log4j.Logger;

import com.ygg.common.exception.CacheException;

public class MemcacheServiceImpl implements CacheServiceIF
{
    
    private static Logger log = Logger.getLogger(MemcacheServiceImpl.class);
    
    private MemcachedClient mc = null;
    
    MemcacheServiceImpl(String host, String port) throws IOException
    {
        mc = new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(host + ":" + port));
    }
    
    @Override
    public boolean delete(String key) throws CacheException
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
    
}
