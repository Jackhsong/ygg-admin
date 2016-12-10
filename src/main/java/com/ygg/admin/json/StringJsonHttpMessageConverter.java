package com.ygg.admin.json;

import java.nio.charset.Charset;

import org.springframework.http.converter.StringHttpMessageConverter;

public class StringJsonHttpMessageConverter extends StringHttpMessageConverter
{
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    
    public StringJsonHttpMessageConverter()
    {
        super(DEFAULT_CHARSET);
    }
    
    public StringJsonHttpMessageConverter(Charset defaultCharset)
    {
        super(defaultCharset);
    }
}
