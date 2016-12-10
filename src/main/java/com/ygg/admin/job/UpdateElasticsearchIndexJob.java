package com.ygg.admin.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.service.IndexSettingService;

public class UpdateElasticsearchIndexJob extends AbstractJobService
{
    Logger log = Logger.getLogger(UpdateElasticsearchIndexJob.class);
    
    @Resource
    private IndexSettingService indexSettingService;
    
    @Override
    public String getDescription()
    {
        return "更新Elasticsearch索引任务";
    }
    
    @Override
    public void doExecute()
        throws Exception
    {
        String timerSwitch = YggAdminProperties.getInstance().getPropertie("timer_switch");
        if ("1".equals(timerSwitch))
        {
            indexSettingService.updateSearchIndex();
        }
        else
        {
            log.info("定时器开关关闭，不执行任务");
        }
    }
}
