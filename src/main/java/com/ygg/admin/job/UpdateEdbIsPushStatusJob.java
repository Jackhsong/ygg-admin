package com.ygg.admin.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.service.OrderService;

public class UpdateEdbIsPushStatusJob extends AbstractJobService
{
    Logger log = Logger.getLogger(UpdateEdbIsPushStatusJob.class);
    
    @Resource(name = "orderService")
    private OrderService orderService;
    
    @Override
    public String getDescription()
    {
        return "更新EDB订单是否推送任务";
    }
    
    @Override
    public void doExecute()
        throws Exception
    {
        String timerSwitch = YggAdminProperties.getInstance().getPropertie("timer_switch");
        if ("1".equals(timerSwitch))
        {
            orderService.updateEdbIsPushStatus();
        }
        else
        {
            log.info("定时器开关关闭，不执行任务");
        }
    }
}
