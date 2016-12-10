package com.ygg.common.services.cache;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

public class CacheManager
{
    private CacheManager()
    {
    }
    
    private static ConcurrentMap<String, CacheServiceIF> cacheClientMap = new ConcurrentHashMap<String, CacheServiceIF>();
    
    private static Logger log = Logger.getLogger(CacheManager.class);
    
    public static void init(String cacheConfig)
    {
        if (cacheClientMap.containsKey(cacheConfig))
        {
            return;
        }
        try
        {
            Properties p = new Properties();
            p.load(CacheManager.class.getClassLoader().getResourceAsStream(cacheConfig + ".properties"));
            
            CacheServiceIF cacheClient = null;
            if (p.getProperty("type").equals("memcache"))
            {
                cacheClient = new MemcacheServiceImpl(p.getProperty("host"), p.getProperty("port"));
            }
            cacheClientMap.put(cacheConfig, cacheClient);
        }
        catch (Exception e)
        {
            log.error("初始化spyMemcache连接出错", e);
        }
    }
    
    public static CacheServiceIF getClient(String dbConfig)
    {
        if (!cacheClientMap.containsKey(dbConfig))
        {
            init(dbConfig);
        }
        return cacheClientMap.get(dbConfig);
    }
    
}
