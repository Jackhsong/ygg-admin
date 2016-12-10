package com.ygg.admin.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.service.OrderSignRecordService;

public class SendOrderSignMessageJob extends AbstractJobService
{
    Logger log = Logger.getLogger(SendOrderSignMessageJob.class);
    
    @Resource
    private OrderSignRecordService orderSignRecordService;
    
    @Override
    public String getDescription()
    {
        return "发送签收短息任务";
    }
    
    @Override
    public void doExecute()
        throws Exception
    {
        String timerSwitch = YggAdminProperties.getInstance().getPropertie("timer_switch");
        if ("1".equals(timerSwitch))
        {
            orderSignRecordService.sendMessage();
        }
        else
        {
            log.info("定时器开关关闭，不执行任务");
        }
    }
}
