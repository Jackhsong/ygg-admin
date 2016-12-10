package com.ygg.admin.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface WechatGroupDataDao
{
    public List<JSONObject> customSql(JSONObject parameter);
}
