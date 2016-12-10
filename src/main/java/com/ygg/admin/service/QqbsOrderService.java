package com.ygg.admin.service;

import java.util.Map;

public interface QqbsOrderService
{

    Map<String, Object> findOrderList(String orderNumber, String orderStatus, String accountId, int page, int rows);
    
}
