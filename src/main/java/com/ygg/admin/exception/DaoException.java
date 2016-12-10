package com.ygg.admin.exception;

@SuppressWarnings("serial")
public class DaoException extends RuntimeException
{
    
    public DaoException()
    {
        super();
    }
    
    public DaoException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public DaoException(String message)
    {
        super(message);
    }
    
    public DaoException(Throwable cause)
    {
        super(cause);
    }
    
}