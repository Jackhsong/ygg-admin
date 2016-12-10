package com.ygg.common.exception;

@SuppressWarnings("serial")
public class CacheException extends Exception
{
    
    public CacheException()
    {
        super();
    }
    
    public CacheException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public CacheException(String message)
    {
        super(message);
    }
    
    public CacheException(Throwable cause)
    {
        super(cause);
    }
    
}