package com.ygg.admin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TestJson
{
    public static void main(String[] args)
    {
        String str1 = "{\"Status\":\"未签收\",\"TrackList\":[{\"ShortStatus\":\"收件\",\"TrackDate\":\"2013-12-22 15:20:23\",";
        String str2 = "\"TrackStatus\":\"浙江省杭州市市场部公司已收件\"},{\"ShortStatus\":\"在途\",\"TrackDate\":\"2013-12-22 21:09:53\",";
        String str3 = "\"TrackStatus\":\"杭州转运中心公司已收入\"},{\"ShortStatus\":\"在途\",\"TrackDate\":\"2013-12-22 21:23:54\",";
        String str4 = "\"TrackStatus\":\"杭州转运中心公司已打包\"}],\"UpdateDate\":\"\\/Date(1387765229084 0800)\\/\"}";
        JSONObject obj = JSON.parseObject(str1 + str2 + str3 + str4);
        String status = obj.getString("Status");
        System.out.println(status);
        JSONArray arry = obj.getJSONArray("TrackList");
        for (int i = 0; i < arry.size(); i++)
        {
            JSONObject o = arry.getJSONObject(i);
            System.out.println(o.getString("ShortStatus"));
            System.out.println(o.getString("TrackDate"));
            System.out.println(o.getString("TrackStatus"));
            System.out.println("----------------");
        }
        System.out.println(arry);
    }
}
