package com.ygg.admin.service;

import java.util.Map;

public interface PushService
{
    Object push(Map<String, String> param, String[] platfrom);
}
