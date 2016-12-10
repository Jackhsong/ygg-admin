package com.ygg.admin.code;

public enum UrlEnum
{
    SinglePro("http://m.gegejia.com/item-"),
    
    GroupPro("http://m.gegejia.com/sale-"),
    
    SingleProQqbs("http://globalscanner.waibao58.com/ygg-hqbs/product/single/"),
    
    GroupProQqbs("http://globalscanner.waibao58.com/ygg-hqbs/cnty/toac/"),
    
    SingleProYw("http://wx.51yanwang.com/ygg-yw/product/single/"),
    
    GroupProYw("http://wx.51yanwang.com/ygg-yw/cnty/toac/");

    public String URL;
    
    private UrlEnum(String url)
    {
        this.URL = url;
    }
    
}
