package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface QqbsOrderDao
{

    List<Map<String, Object>> findOrderList(Map<String, Object> param);

    int countOrderList(Map<String, Object> param);
    
}
